package instructionselection;

import cs132.vapor.ast.VaporProgram;
import cs132.vapor.parser.VaporParser;

public class VM2M {
    public static void main(String[] args) {
        instructionSelection();
    }

    public static void instructionSelection() {
        try {
            VaporProgram vaporProgram = ParseVapor.parseVapor(System.in, System.err);


        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace(System.out);
        }
    }
}
