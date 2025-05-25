package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test for Create Shipment User Story
 */
public class CreateShipmentTest {

    private Shipment testShipment;
    private int testOrderID = 1;
    private int testCustomerID = 1;
    private String testShipmentMethod = "Express";
    private String testStreetAddress = "123 Test St";
    private String testCity = "Sydney";
    private String testState = "NSW";
    private String testPostcode = "2000";

    @Before
    public void setUp() {
        // Initialize test shipment
        testShipment = new Shipment(testOrderID, testCustomerID, testShipmentMethod,
                testStreetAddress, testCity, testState, testPostcode);
    }

    @Test
    public void testCreateShipmentWithValidData() {
        // Test that shipment is created
        assertNotNull("Shipment should be created", testShipment);
        assertEquals("Order ID should match", testOrderID, testShipment.getOrderID());
        assertEquals("Customer ID should match", testCustomerID, testShipment.getCustomerID());
        assertEquals("Shipment method should match", testShipmentMethod, testShipment.getShipmentMethod());
        assertEquals("Street address should match", testStreetAddress, testShipment.getStreetAddress());
        assertEquals("City should match", testCity, testShipment.getCity());
        assertEquals("State should match", testState, testShipment.getState());
        assertEquals("Postcode should match", testPostcode, testShipment.getPostcode());
    }

    @Test
    public void testShipmentStatusPending() {
        // Test that new shipment has "Pending" status
        assertEquals("Status should be Pending", "Pending", testShipment.getStatus());
    }

    @Test
    public void testShipmentNotFinalized() {
        // Test that new shipment is not finalized
        assertFalse("Shipment should not be finalized", testShipment.isFinalized());
    }

    @Test
    public void testShipmentIDGreaterThanZero() {
        // Test that shipment ID is greater than 0
        testShipment.setShipmentID(1);
        assertTrue("Shipment ID should be greater than 0", testShipment.getShipmentID() > 0);
    }

    @Test
    public void testGetFullAddress() {
        // Test full address concatenation
        String expectedAddress = testStreetAddress + ", " + testCity + ", " + testState + " " + testPostcode;
        assertEquals("Full address should be formatted correctly", expectedAddress, testShipment.getFullAddress());
    }

    @Test
    public void testShipmentConstructorWithAllFields() {
        // Test constructor with all parameters
        Shipment fullShipment = new Shipment(1, 2, 3, "Standard", null, "456 Oak Ave", 
                "Melbourne", "VIC", "3000", "Processing", "TRK123", null, null, true);
        
        assertEquals("Shipment ID should match", 1, fullShipment.getShipmentID());
        assertEquals("Order ID should match", 2, fullShipment.getOrderID());
        assertEquals("Customer ID should match", 3, fullShipment.getCustomerID());
        assertEquals("Method should match", "Standard", fullShipment.getShipmentMethod());
        assertEquals("Status should match", "Processing", fullShipment.getStatus());
        assertEquals("Tracking number should match", "TRK123", fullShipment.getTrackingNumber());
        assertTrue("Should be finalized", fullShipment.isFinalized());
    }

    @Test
    public void testToString() {
        // Test toString method
        testShipment.setShipmentID(1);
        String result = testShipment.toString();
        
        assertTrue("Should contain shipment ID", result.contains("shipmentID=1"));
        assertTrue("Should contain order ID", result.contains("orderID=" + testOrderID));
        assertTrue("Should contain customer ID", result.contains("customerID=" + testCustomerID));
        assertTrue("Should contain method", result.contains("shipmentMethod='" + testShipmentMethod + "'"));
        assertTrue("Should contain status", result.contains("status='Pending'"));
        assertTrue("Should contain finalized status", result.contains("finalized=false"));
    }
}