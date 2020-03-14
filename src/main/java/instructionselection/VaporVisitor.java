package instructionselection;

import cs132.vapor.ast.*;
import cs132.vapor.ast.VInstr.Visitor;

import java.util.List;
import java.util.ArrayList;

public class VaporVisitor <E extends Throwable> extends Visitor<E> {

    VFunction currFunc;
    List<String> buffer;

    /*
        Interface functions
    */

    public void setData(VFunction currFunc) {
        this.currFunc = currFunc;
        buffer = new ArrayList<>();
    }

    public void printBuffer() {
        for (String line : buffer) {
            System.out.println(line);
        }
    }

    /*
        Helper functions
    */

    void addLine(String line) {
        buffer.add(line);
    }

    /*
        Visitor functions
    */

    public void visit(VAssign a) throws E {
        String currLine = "";

        String destReg = a.dest.toString();

        if (a.source instanceof VVarRef) {
            currLine += "mov " + destReg + " " + a.source.toString();
        } else if (a.source instanceof VOperand.Static) {
            currLine += "li " + destReg + " " + a.source.toString();
        }

        addLine(currLine);
    }

    public void visit(VCall c) throws E {
        String currLine = "";

        if (c.addr instanceof VAddr.Var) {
            currLine += "jalr " + c.addr.toString();
        } else {

        }

        addLine(currLine);
    }

    public void visit(VBuiltIn c) throws E {

    }

    public void visit(VMemWrite w) throws E {
    }

    public void visit(VMemRead r) throws E {
    }

    public void visit(VBranch b) throws E {
    }

    public void visit(VGoto g) throws E {
    }

    public void visit(VReturn r) throws E {
    }
}
