package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.AddCustomerCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

/**
 * Parses input arguments and creates a new AddCustomerCommand object.
 * This class is responsible for extracting the necessary customer details
 * from the user's input string to facilitate the registration of a new customer into the system.
 */
public class AddCustomerCommandParser implements Parser<AddCustomerCommand> {

    /**
     * Parses the given arguments string in the context of the add customer command
     * and returns an AddCustomerCommand object for execution.
     *
     * @param description The string containing the arguments provided by the user.
     * @return An AddCustomerCommand object instantiated with the extracted customer details.
     * @throws PharmaTrackerException If the user input is invalid.
     */
    @Override
    public AddCustomerCommand parse(String description) throws PharmaTrackerException {
        String id = CustomerParserUtil.extractCustomerID(description);
        String customerName = CustomerParserUtil.extractCustomerName(description);
        String phone = CustomerParserUtil.extractCustomerPhone(description);
        String address = CustomerParserUtil.extractCustomerAddress(description);
        return new AddCustomerCommand(id, customerName, phone, address);
    }
}
