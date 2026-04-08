package seedu.pharmatracker.parser;

import seedu.pharmatracker.command.AddCustomerCommand;
import seedu.pharmatracker.exceptions.PharmaTrackerException;

public class AddCustomerCommandParser implements Parser<AddCustomerCommand> {
    @Override
    public AddCustomerCommand parse(String description) throws PharmaTrackerException {
        String id = CustomerParserUtil.extractCustomerID(description);
        String customerName = CustomerParserUtil.extractCustomerName(description);
        String phone = CustomerParserUtil.extractCustomerPhone(description);
        String address = CustomerParserUtil.extractCustomerAddress(description);
        return new AddCustomerCommand(id, customerName, phone, address);
    }
}
