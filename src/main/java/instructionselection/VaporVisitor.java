package instructionselection;

import cs132.vapor.ast.*;
import cs132.vapor.ast.VInstr.Visitor;

import java.util.List;
import java.util.ArrayList;

public class VaporVisitor <E extends Throwable> extends Visitor<E> {

    VFunction currFunc;
    List<String> buffer;
    //int lineCount;

    /*
        Interface functions
    */

    public void setData(VFunction currFunc) {
        this.currFunc = currFunc;
        buffer = new ArrayList<>();

        for (int i = 0; i <= currFunc.body.length + currFunc.labels.length; i++) {
            buffer.add("");
        }

        //lineCount = 0;
    }

    public void printBuffer() {
        insertLabels();

        for (String line : buffer) {
            System.out.println(line);
        }
    }

    /*
        Helper functions
    */

    int getRelativePos(int sourcePos) {
        return (sourcePos - currFunc.sourcePos.line) - 1;
    }

    void insertLabels() {
        for (int i = 0; i < currFunc.labels.length; i++) {
            buffer.set(getRelativePos(currFunc.labels[i].sourcePos.line), currFunc.labels[i].ident + ":");
        }
    }

    void addLine(int pos, String line) {
        buffer.set(pos,line);
    }

    /*
        Visitor functions
    */

    public void visit(VAssign a) throws E {
        String currLine = "";
        int relPos = getRelativePos(a.sourcePos.line);

        String destReg = a.dest.toString();

        if (a.source instanceof VVarRef) {
            currLine += "mov " + destReg + " " + a.source.toString();
        } else if (a.source instanceof VOperand.Static) {
            currLine += "li " + destReg + " " + a.source.toString();
        }

        addLine(relPos, currLine);
    }

    public void visit(VCall c) throws E {
        String currLine = "";
        int relPos = getRelativePos(c.sourcePos.line);

        if (c.addr instanceof VAddr.Var) {
            currLine += "jalr " + c.addr.toString();
        } else {

        }

        addLine(relPos, currLine);
    }

    public void visit(VBuiltIn c) throws E {
        String currLine = "";
        int relPos = getRelativePos(c.sourcePos.line);

        switch (c.op.name) {
            case "Add":
                currLine += "addu " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                break;
            case "Sub":
                currLine += "subu " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                break;
            case "LtS":
                currLine += "slti " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                break;
            case "MulS":
                currLine += "mul " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                break;
            case "PrintIntS":
                currLine += "move $a0 " + c.args[0].toString() + "\n";
                currLine += "jal _print";
                break;
            case "HeapAllocZ":
                currLine += "li $a0 " + c.args[0].toString() + "\n";
                currLine += "jal _heapAlloc";
                break;
            case "Error":
                currLine += "la $a0 _str0\n";
                currLine += "j _error";
                break;
            default:
                currLine += "(" + c.op.name + ") UNDEFINED";
                break;
        }

        addLine(relPos, currLine);
    }

    public void visit(VMemWrite w) throws E {
        String currLine = "";
        int relPos = getRelativePos(w.sourcePos.line);

        addLine(relPos, currLine);
    }

    public void visit(VMemRead r) throws E {
        String currLine = "";
        int relPos = getRelativePos(r.sourcePos.line);

        addLine(relPos, currLine);
    }

    public void visit(VBranch b) throws E {
        String currLine = "";
        int relPos = getRelativePos(b.sourcePos.line);

        addLine(relPos, currLine);
    }

    public void visit(VGoto g) throws E {
        String currLine = "";
        int relPos = getRelativePos(g.sourcePos.line);

        addLine(relPos, currLine);
    }

    public void visit(VReturn r) throws E {
        String currLine = "";
        int relPos = getRelativePos(r.sourcePos.line);

        addLine(relPos, currLine);
    }
}
