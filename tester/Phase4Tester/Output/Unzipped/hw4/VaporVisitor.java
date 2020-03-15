

import cs132.vapor.ast.*;
import cs132.vapor.ast.VInstr.Visitor;

import java.util.List;
import java.util.ArrayList;

public class VaporVisitor <E extends Throwable> extends Visitor<E> {

    VFunction currFunc;
    List<String> buffer;
    int spSize;

    /*
        Interface functions
    */

    public void setData(VFunction currFunc) {
        this.currFunc = currFunc;
        buffer = new ArrayList<>();
        spSize = (currFunc.stack.out * 4) + (currFunc.stack.local*4) + 8;
        for (int i = 0; i <= currFunc.body.length + currFunc.labels.length; i++) {
            buffer.add("");
        }
    }

    public void printBuffer() {
        insertLabels();

        // on entry
        String onEntry = "";
        onEntry += "sw $fp -8($sp)\n";
        onEntry += "move $fp $sp\n";
        onEntry += "subu $sp $sp " + Integer.toString(spSize) + "\n";
        onEntry += "sw $ra -4($fp)";

        buffer.add(0, onEntry);

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
            currLine += "move " + destReg + " " + a.source.toString();
        } else if (a.source instanceof VLitInt) {
            currLine += "li " + destReg + " " + a.source.toString();
        } else {
            currLine += "la " + destReg + " " + a.source.toString().replace(":", "");
        }

        addLine(relPos, currLine);
    }

    public void visit(VCall c) throws E {
        String currLine = "";
        int relPos = getRelativePos(c.sourcePos.line);

        if (c.addr instanceof VAddr.Var) {
            currLine += "jalr " + c.addr.toString();
        } else {
            currLine += "jal " + c.addr.toString().replace(":","");
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
                if (c.args[0] instanceof  VOperand.Static) {
                    currLine += "addi " + c.dest.toString() + " " + c.args[1].toString() + " " + c.args[0].toString();
                } else {
                    currLine += "subu " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                }
                break;
            case "LtS":
                if (c.args[1] instanceof VOperand.Static) {
                    currLine += "slti " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                } else {
                    currLine += "slt " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                }
                break;
            case "MulS":
                currLine += "mul " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString();
                break;
            case "PrintIntS":
                if (c.args[0] instanceof VVarRef) {
                    currLine += "move $a0 " + c.args[0].toString() + "\n";
                } else {
                    currLine += "li $a0 " + c.args[0].toString() + "\n";
                }
                currLine += "jal _print";
                break;
            case "HeapAllocZ":
                currLine += "li $a0 " + c.args[0].toString() + "\n";
                currLine += "jal _heapAlloc\n";
                currLine += "move " + c.dest.toString() + " $v0";
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

        String source = w.source.toString().replace(":", "");
        String dest;
        String offset = "0";
        if (w.dest instanceof VMemRef.Global) {
            dest = ((VMemRef.Global) w.dest).base.toString();
            offset = Integer.toString(((VMemRef.Global) w.dest).byteOffset);
        } else {
            dest = "$sp";
            offset = Integer.toString(((VMemRef.Stack) w.dest).index * 4);
        }

        if (w.source instanceof VVarRef) {
            currLine += "sw " + w.source.toString() + " " + (offset) + "(" + dest + ")";
        } else {
            currLine += "la $t9 " + source + "\n";
            currLine += "sw $t9 " + offset + "(" + dest + ")";
        }


        addLine(relPos, currLine);
    }

    public void visit(VMemRead r) throws E {
        String currLine = "";
        int relPos = getRelativePos(r.sourcePos.line);

        String source = "";
        String offset = "0";

        if (r.source instanceof VMemRef.Global) {
            source = ((VMemRef.Global) r.source).base.toString();
            offset = Integer.toString(((VMemRef.Global) r.source).byteOffset);
        } else if (r.source instanceof VMemRef.Stack) {
            // Stack
            source = "$sp";
            offset = Integer.toString(((VMemRef.Stack) r.source).index * 4);
        }

        currLine += "lw " + r.dest.toString() + " " + offset + "(" + source + ")";

        addLine(relPos, currLine);
    }

    public void visit(VBranch b) throws E {
        String currLine = "";
        int relPos = getRelativePos(b.sourcePos.line);

        if (b.positive) {
            currLine += "bnez " + b.value.toString() + " " + b.target.ident;
        } else {
            currLine += "beqz " + b.value.toString() + " " + b.target.ident;
        }

        addLine(relPos, currLine);
    }

    public void visit(VGoto g) throws E {
        String currLine = "";
        int relPos = getRelativePos(g.sourcePos.line);

        currLine += "j " + g.target.toString().substring(1);

        addLine(relPos, currLine);
    }

    public void visit(VReturn r) throws E {
        String currLine = "";
        int relPos = getRelativePos(r.sourcePos.line);

        if (r.value != null) {
            if (r.value instanceof VVarRef) {
                currLine += "move $v0 " + r.value.toString() + "\n";
            } else if (r.value instanceof VOperand.Static) {
                currLine += "li $v0 " + r.value.toString() + "\n";
            }
        }

        // on exit
        currLine += "lw $ra -4($fp)\n";
        currLine += "lw $fp -8($fp)\n";
        currLine += "addu $sp $sp " + spSize + "\n";

        currLine += "jr $ra";

        addLine(relPos, currLine);
    }
}
