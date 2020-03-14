package instructionselection;

import cs132.vapor.ast.*;
import cs132.vapor.ast.VInstr.Visitor;

public class VaporVisitor <E extends Throwable> extends Visitor<E> {

    VFunction currFunc;

    /*
        Helper functions
    */

    public void setData(VFunction currFunc) {
        this.currFunc = currFunc;
    }

    /*
        Visitor functions
    */

    public void visit(VAssign a) throws E {
        System.out.println(a.dest.toString() + " = " + a.source.toString());
    }

    public void visit(VCall c) throws E {
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
