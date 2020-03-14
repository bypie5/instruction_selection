

import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VaporProgram;

public class VM2M {
    public static void main(String[] args) {
        instructionSelection();
    }

    public static void instructionSelection() {
        try {
            VaporProgram tree = ParseVapor.parseVapor(System.in, System.err);

            // Translate the data section
            System.out.println(".data");
            for (int i = 0; i < tree.dataSegments.length; i++) {
                System.out.println(tree.dataSegments[i].ident);
                for (int j = 0; j < tree.dataSegments[i].values.length; j++) {
                    System.out.println("    " + tree.dataSegments[i].values[j]);
                }
            }
            System.out.println("");

            // Translate the text section
            System.out.println(".text");

            // Jump-and-link to Main
            System.out.println("jal Main\nli $v0 10\nsyscall\n");

            // Translate function bodies
            VaporVisitor<Exception> vaporVisitor = new VaporVisitor<>();
            for (int i = 0; i < tree.functions.length; i++) {
                VFunction currFunction = tree.functions[i];
                vaporVisitor.setData(currFunction);

                // Translate function header
                System.out.println(currFunction.ident + ":");

                // For each line in each function in the program
                for (int j = 0; j < currFunction.body.length; j++) {
                    currFunction.body[j].accept(vaporVisitor);
                }

                vaporVisitor.printBuffer();

                System.out.println(); // newline for readability
            }

            // Translate useful syscalls and data
            System.out.println("\n_print:\nli $v0 1\nsyscall\nla $a0 _newline\nli $v0 4\nsyscall\njr $ra");
            System.out.println("\n_error:\nli $v0 4\nsyscall\nli $v0 10\nsyscall");
            System.out.println("\n_heapAlloc:\nli $v0 9\nsyscall\njr $ra");
            System.out.println("\n.data");
            System.out.println(".align 0");
            System.out.println("_newline: .asciiz \"\\n\"");
            System.out.println("_str0: .asciiz \"null pointer\\n\"");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace(System.out);
        }
    }
}
