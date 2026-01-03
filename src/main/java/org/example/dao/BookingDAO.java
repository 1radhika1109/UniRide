package org.example.dao;

import org.example.exception.SeatUnavailableException;
import org.example.model.Booking;
import org.example.model.Ride;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class BookingDAO {

    private final SessionFactory sessionFactory;

    public BookingDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save a new booking
    public void saveBooking(Booking booking) throws SeatUnavailableException {
        if (booking.getRide() == null || booking.getPassenger() == null) {
            throw new IllegalArgumentException("Ride or Passenger cannot be null!");
        }

        Ride ride = booking.getRide();

        if (ride.getAvailableSeats() <= 0) {
            throw new SeatUnavailableException("No seats available for this ride!");
        }

        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Decrease ride seats
            ride.setAvailableSeats(ride.getAvailableSeats() - 1);
            session.update(ride);

            // Save booking
            session.persist(booking);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Cancel booking
    public void deleteBooking(Booking booking) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Optionally increase ride seats back
            Ride ride = booking.getRide();
            if (ride != null) {
                ride.setAvailableSeats(ride.getAvailableSeats() + 1);
                session.update(ride);
            }

            session.delete(booking);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Booking", Booking.class).list();
        }
    }

    // Get bookings by passenger
    public List<Booking> getBookingsByPassenger(int passengerId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Booking b WHERE b.passenger.id = :pid", Booking.class)
                    .setParameter("pid", passengerId)
                    .list();
        }
    }

    // Get bookings by ride
    public List<Booking> getBookingsByRide(int rideId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Booking b WHERE b.ride.id = :rid", Booking.class)
                    .setParameter("rid", rideId)
                    .list();
        }
    }
}
