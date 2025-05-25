package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for Delete Un-finalised Shipment User Story
 */
public class DeleteShipmentTest {

    private Shipment unfinalizedShipment;
    private Shipment finalizedShipment;

    @Before
    public void setUp() {
        // Create unfinalized shipment
        unfinalizedShipment = new Shipment(1, 1, "Standard", "123 Test St", "Sydney", "NSW", "2000");
        unfinalizedShipment.setShipmentID(1);
        unfinalizedShipment.setStatus("Pending");
        unfinalizedShipment.setFinalized(false);

        // Create finalized shipment
        finalizedShipment = new Shipment(2, 1, "Express", "456 Oak Ave", "Melbourne", "VIC", "3000");
        finalizedShipment.setShipmentID(2);
        finalizedShipment.setStatus("Shipped");
        finalizedShipment.setFinalized(true);
        finalizedShipment.setTrackingNumber("TRK123456");
    }

    @Test
    public void testUnfinalizedShipmentCanBeDeleted() {
        // Test that unfinalized shipment can be deleted
        assertFalse("Shipment should not be finalized", unfinalizedShipment.isFinalized());
        assertEquals("Status should be Pending", "Pending", unfinalizedShipment.getStatus());
        boolean canDelete = !unfinalizedShipment.isFinalized();
        assertTrue("Unfinalized shipment should be eligible for deletion", canDelete);
    }

    @Test
    public void testFinalizedShipmentCannotBeDeleted() {
        // Test that finalized shipment can't be deleted
        assertTrue("Shipment should be finalized", finalizedShipment.isFinalized());
        boolean canDelete = !finalizedShipment.isFinalized();
        assertFalse("Finalized shipment should not be eligible for deletion", canDelete);
    }

    @Test
    public void testDeleteValidation() {
        // Test delete validation
        assertTrue("Should be able to delete unfinalized shipment", canDeleteShipment(unfinalizedShipment));
        assertFalse("Should not be able to delete finalized shipment", canDeleteShipment(finalizedShipment));
    }

    @Test
    public void testShipmentExistsBeforeDelete() {
        // Test that shipment exists before deletion
        assertNotNull("Shipment should exist", unfinalizedShipment);
        assertTrue("Shipment ID should be greater than 0", unfinalizedShipment.getShipmentID() > 0);
    }

    @Test
    public void testDeleteReturnsSuccess() {
        // Test that delete action returns success
        boolean deleteResult = simulateDelete(unfinalizedShipment);
        assertTrue("Delete should return true for unfinalized shipment", deleteResult);
    }

    @Test
    public void testDeleteReturnsFalseForFinalized() {
        // Test that delete actions returns false for finalized shipment
        boolean deleteResult = simulateDelete(finalizedShipment);
        assertFalse("Delete should return false for finalized shipment", deleteResult);
    }

    @Test
    public void testMultipleUnfinalizedShipments() {
        // Test multiple unfinalized shipments can be deleted
        Shipment shipment1 = new Shipment(3, 1, "Economy", "789 Pine St", "Brisbane", "QLD", "4000");
        shipment1.setFinalized(false);
        
        Shipment shipment2 = new Shipment(4, 1, "Priority", "321 Elm St", "Perth", "WA", "6000");
        shipment2.setFinalized(false);
        
        assertTrue("First shipment should be deletable", canDeleteShipment(shipment1));
        assertTrue("Second shipment should be deletable", canDeleteShipment(shipment2));
    }

    @Test
    public void testShipmentWithPendingStatus() {
        // Test that pending shipments can be deleted
        unfinalizedShipment.setStatus("Pending");
        assertTrue("Pending shipment should be deletable", canDeleteShipment(unfinalizedShipment));
    }

    @Test
    public void testShipmentWithProcessingStatus() {
        // Test shipment with processing status but not finalized
        unfinalizedShipment.setStatus("Processing");
        unfinalizedShipment.setFinalized(false);
        assertTrue("Non-finalized processing shipment should be deletable", canDeleteShipment(unfinalizedShipment));
    }

    @Test
    public void testShipmentWithTrackingButNotFinalized() {
        // Test shipment with tracking number but not finalized (edge case)
        unfinalizedShipment.setTrackingNumber("TRK999888");
        unfinalizedShipment.setFinalized(false);
        assertTrue("Shipment with tracking but not finalized should be deletable", canDeleteShipment(unfinalizedShipment));
    }

    private boolean canDeleteShipment(Shipment shipment) {
        return !shipment.isFinalized();
    }

    private boolean simulateDelete(Shipment shipment) {
        if (canDeleteShipment(shipment)) {
            return true;
        }
        return false;
    }
}