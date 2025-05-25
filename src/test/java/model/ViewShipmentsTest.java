package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit Test for View Shipments User Story
 */
public class ViewShipmentsTest {

    private List<Shipment> testShipments;
    private Shipment shipment1;
    private Shipment shipment2;
    private Shipment shipment3;

    @Before
    public void setUp() {
        // Create test shipments for customer ID 1
        shipment1 = new Shipment(1, 1, "Express", "123 Test St", "Sydney", "NSW", "2000");
        shipment1.setShipmentID(1);
        shipment1.setStatus("Pending");
        
        shipment2 = new Shipment(2, 1, "Standard", "456 Oak Ave", "Melbourne", "VIC", "3000");
        shipment2.setShipmentID(2);
        shipment2.setStatus("Processing");
        shipment2.setTrackingNumber("TRK123456");
        
        shipment3 = new Shipment(3, 1, "Priority", "789 Pine Rd", "Brisbane", "QLD", "4000");
        shipment3.setShipmentID(3);
        shipment3.setStatus("Shipped");
        shipment3.setTrackingNumber("TRK789012");
        shipment3.setFinalized(true);

        // Create list of shipments
        testShipments = new ArrayList<>();
        testShipments.add(shipment1);
        testShipments.add(shipment2);
        testShipments.add(shipment3);
    }

    @Test
    public void testGetShipmentsByCustomerId() {
        // Test that all shipments belong to customer 1
        for (Shipment shipment : testShipments) {
            assertEquals("All shipments should belong to customer 1", 1, shipment.getCustomerID());
        }
        
        assertEquals("Should have 3 shipments", 3, testShipments.size());
    }

    @Test
    public void testShipmentListNotNull() {
        // Test that shipments list is not null
        assertNotNull("Shipments list should not be null", testShipments);
        assertFalse("Shipments list should not be empty", testShipments.isEmpty());
    }

    @Test
    public void testShipmentDetailsDisplay() {
        // Test that each shipment has alll parameters
        for (Shipment shipment : testShipments) {
            assertNotNull("Shipment ID should not be null", shipment.getShipmentID());
            assertNotNull("Order ID should not be null", shipment.getOrderID());
            assertNotNull("Status should not be null", shipment.getStatus());
            assertNotNull("Street address should not be null", shipment.getStreetAddress());
            assertNotNull("City should not be null", shipment.getCity());
            assertNotNull("State should not be null", shipment.getState());
            assertNotNull("Postcode should not be null", shipment.getPostcode());
        }
    }

    @Test
    public void testShipmentStatusValues() {
        // Test status values are valid
        assertEquals("First shipment should be Pending", "Pending", shipment1.getStatus());
        assertEquals("Second shipment should be Processing", "Processing", shipment2.getStatus());
        assertEquals("Third shipment should be Shipped", "Shipped", shipment3.getStatus());
    }

    @Test
    public void testTrackingNumberVisibility() {
        // Test tracking number is only for finalized shipments
        assertNull("Pending shipment should not have tracking number", shipment1.getTrackingNumber());
        assertNotNull("Processing shipment should have tracking number", shipment2.getTrackingNumber());
        assertNotNull("Shipped shipment should have tracking number", shipment3.getTrackingNumber());
        
        assertEquals("Processing shipment tracking should match", "TRK123456", shipment2.getTrackingNumber());
        assertEquals("Shipped shipment tracking should match", "TRK789012", shipment3.getTrackingNumber());
    }

    @Test
    public void testAddressInformation() {
        // Test valid address information
        assertEquals("First shipment address", "123 Test St, Sydney, NSW 2000", shipment1.getFullAddress());
        assertEquals("Second shipment address", "456 Oak Ave, Melbourne, VIC 3000", shipment2.getFullAddress());
        assertEquals("Third shipment address", "789 Pine Rd, Brisbane, QLD 4000", shipment3.getFullAddress());
    }

    @Test
    public void testShipmentShortDetails() {
        // Test abbreviated details format for shipment-list display
        String details1 = shipment1.getShortDetails();
        String details2 = shipment2.getShortDetails();
        String details3 = shipment3.getShortDetails();
        
        assertTrue("Should contain shipment ID", details1.contains("Shipment #1"));
        assertTrue("Should contain status", details1.contains("Pending"));
        
        assertTrue("Should contain tracking number", details2.contains("TRK123456"));
        assertTrue("Should contain tracking number", details3.contains("TRK789012"));
    }

    @Test
    public void testFinalizedShipments() {
        // Test finalized status
        assertFalse("Pending shipment should not be finalized", shipment1.isFinalized());
        assertFalse("Processing shipment should not be finalized", shipment2.isFinalized());
        assertTrue("Shipped shipment should be finalized", shipment3.isFinalized());
    }
}