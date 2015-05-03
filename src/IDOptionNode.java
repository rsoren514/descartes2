import java.io.IOException;


public class IDOptionNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;


    //=========//
    // Methods //
    //=========//

    public IDOptionNode(String id) {
        m_id = id;
    }

    public void popLoopID(ProgState progState) {
        String id;


        // If the ID is null, only pop the most recent loop id off
        // the stack.
        if (m_id == null) {
            assert(!progState.loopIDStack().isEmpty());
            id = progState.loopIDStack().pop();
        }

        // If the ID isn't null, keep popping until we pop the ID
        // off the stack.
        else {
            do {
                assert(!progState.loopIDStack().isEmpty());
                id = progState.loopIDStack().pop();
            } while (!id.equals(m_id));
        }

        // Set the break name to ensure stmt-tails don't continue
        // to execute. This causes any execute() methods to return
        // back up to the loop with the break name; it will clear
        // the break name and return to its parent stmt-tail,
        // which will proceed to execute statements following the
        // loop.
        progState.setBreakName(id);
    }


    //================//
    // Static Methods //
    //================//

    public static IDOptionNode parseIDOption(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        IDOptionNode idOption;

        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 16.
        //
        //      id-option : ID
        //

        if (token.getCode() == TokenCode.T_ID) {
            idOption = new IDOptionNode(token.getText());
        }

        //
        // GR 17.
        //
        //      id-option :
        //

        else {
            // The token wasn't an ID, so put it back.
            tokenReader.unread(token);

            // Create an empty id-option.
            idOption = new IDOptionNode(null);
        }


        return idOption;
    }
}
