package org.example.dao;

import org.example.exception.SeatUnavailableException;
import org.example.model.Booking;
import org.example.model.Ride;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RideDAO {

    private final SessionFactory sessionFactory;

    public RideDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save new ride
    public void saveRide(Ride ride) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(ride);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Update ride
    public void updateRide(Ride ride) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(ride);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Delete ride
    public void deleteRide(Ride ride) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.remove(ride); // or session.delete(ride);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Get ride by ID
    public Ride getRideById(int rideId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Ride.class, rideId);
        }
    }

    // Get all rides
    public List<Ride> getAllRides() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Ride", Ride.class).list();
        }
    }

    // Get all rides by driver
    public List<Ride> getAllRidesByDriver(int driverId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Ride WHERE driver.id = :driverId", Ride.class)
                    .setParameter("driverId", driverId)
                    .list();
        }
    }

    // Search rides (partial, case-insensitive)
    public List<Ride> searchRides(String sourceQuery, String destinationQuery) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Ride r WHERE lower(r.source) LIKE :source " +
                                    "AND lower(r.destination) LIKE :destination " +
                                    "AND r.seatsAvailable > 0", Ride.class)
                    .setParameter("source", "%" + sourceQuery.toLowerCase() + "%")
                    .setParameter("destination", "%" + destinationQuery.toLowerCase() + "%")
                    .list();
        }
    }


    // Reduce seats safely
    public void reduceSeats(Ride ride) throws SeatUnavailableException {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Ride r = session.get(Ride.class, ride.getRideId());
            if (r.getAvailableSeats() <= 0) throw new SeatUnavailableException("No seats available!");
            r.setAvailableSeats(r.getAvailableSeats() - 1);
            session.merge(r);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Save booking
    public void saveBooking(Booking booking) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(booking);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // Get bookings by passenger
    public List<Booking> getBookingsByPassenger(int passengerId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Booking WHERE passenger.id = :pid", Booking.class)
                    .setParameter("pid", passengerId)
                    .list();
        }
    }
}
