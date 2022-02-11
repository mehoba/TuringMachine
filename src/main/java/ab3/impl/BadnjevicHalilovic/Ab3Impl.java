package ab3.impl.BadnjevicHalilovic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ab3.Ab3;
import ab3.TuringMachine;

public class Ab3Impl implements Ab3 {

    private TuringMachine turingMachine;
    protected int currentState, startState, numberOfStates, numberOfTapes;
    protected List<TuringMachine.TapeContent> tapeContents;
    protected boolean isInHaltingState, isInErrorState;
    protected Set<Character> turingAlphabet; // hashset ?
    protected int haltState, initState;
    protected char blankSymbol = '_';
    protected char symbolStartEnd = '$';
    protected TuringMachine.TapeContent tapeContent;
    protected List<Transaction> transactionList = new ArrayList<>();
    protected List<Transaction> multibandTransactionList= new ArrayList<>();
    private int currentStep;


    @Override
    public TuringMachine getEmptyTM() {


        turingMachine = new TuringMachine() {

            @Override
            public void reset() {
                //TODO: * Setzt die Turingmaschine zurück. Dabei wird der Bandinhalt aller Bänder
                //     * gelöscht, der derzeitige Zustand wird auf den Ausgangszustand
                //     * zurückgesetzt, sowie der Schreib-/Lesekopf auf das erste Zeichen des
                //     * Bandes gesetzt. Nach einem erneuten Aufruf von setInput(...) kann also
                //     * eine neue Berechnung beginnen.

                isInErrorState = false;
                isInHaltingState = false;
                currentState = initState;
                Character[] newLeft = new Character[0];
                Character[] newRight = new Character[0];
                tapeContent = new TapeContent(newLeft, null, newRight);
                setInput(null);
                tapeContents = new ArrayList<>();


            }

            @Override
            public int getCurrentState() throws IllegalStateException {
                if (isInErrorState())
                    throw new IllegalStateException();
                else
                    return currentState;
            }

            @Override
            public void setAlphabet(Set<Character> alphabet) throws IllegalArgumentException {
                if (alphabet.contains(null)) {
                    throw new IllegalArgumentException();
                } else {
                    /* Gibt das Input-Alphabet der Turingmaschine. Das Bandalphabet
                     * unterscheidet sich vom Input-Alphabet nur durch das zusätzlich
                     * vorhandene Leerzeichen (das durch "null" repräsentiert wird).*/
                    turingAlphabet = alphabet;
                    transactionList = new ArrayList<>();
                    multibandTransactionList=new ArrayList<>();
                }
            }

            @Override
            public Set<Character> getAlphabet() {
                return turingAlphabet;
            }

            @Override
            public void addTransition(int fromState, Character read, int toState, Character write, Movement move) throws IllegalArgumentException {
                //TODO: * Fügt einen Übergang bei einer TM mit nur einem Band hinzu.
                //     *
                //     * @param fromState Der Ausgangszustand, in dem der Übergang anwendbar ist
                //     * @param read das zu lesenden Zeichen am (einzigen) Band
                //     * @param toState Der Folgezustand, wenn der Übergang ausgeführt wurde
                //     * @param write das zu schreibenden Zeichen am (einzigen) Band
                //     * @param move die Kopf-Bewegungen am (einzigen) Band
                //     *
                //     * @throws IllegalArgumentException
                //     *             Wird geworfen, wenn der Ausgangszustand der Haltezustand
                //     *             ist; wenn ein Übergang nicht deterministisch ist (bzgl.
                //     *             fromState, read); wenn ein Symbol nicht Teil des
                //     *             Bandalphabets ist; wenn ein Zustand nicht existiert; oder
                //     *             wenn die TM mehr als ein Band hat.


                if (fromState == haltState || fromState >= numberOfStates || toState >= numberOfStates || !getAlphabet().contains(read) && read != null || !getAlphabet().contains(write) && write != null)
                    throw new IllegalArgumentException();

                Transaction transaction = new Transaction(fromState, read, toState, write, move);
                transactionList.add(transaction);
            }

            @Override
            public void addTransition(int fromState, Character[] read, int toState, Character[] write, Movement[] move) throws IllegalArgumentException {
                //TODO:  * Fügt einen Übergang hinzu. Dabei gibt das Array read an der Stelle i an,
                //     * was beim Übergang vom Band i jeweils gelesen werden muss (also unter dem
                //     * Schreib-/Lesekopf steht; 0-indexiert).  Das Array write ist analog
                //     * aufgebaut, und gibt an, welches Symbol auf jedes Band jeweils
                //     * geschrieben wird. Das Array move ist ebenfalls analog aufgebaut, und
                //     * beschreibt die Bewegungen der Schreib-/Leseköpfe der jeweiligen Bänder.
                //     *
                //     * @param fromState Der Ausgangszustand, in dem der Übergang anwendbar ist
                //     * @param read Array mit den zu lesenden Zeichen pro Band
                //     * @param toState Der Folgezustand, wenn der Übergang ausgeführt wurde
                //     * @param write Array mit den zu schreibenden Zeichen pro Band
                //     * @param move Array mit den Kopf-Bewegungen pro Band
                //     *
                //     * @throws IllegalArgumentException
                //     *             Wird geworfen, wenn der Ausgangszustand der Haltezustand
                //     *             ist; wenn ein Übergang nicht deterministisch ist (bzgl.
                //     *             fromState, read); wenn ein Symbol nicht Teil des
                //     *             Bandalphabets ist; oder wenn ein Zustand nicht existiert.

                if (fromState == haltState || fromState >= numberOfStates || toState >= numberOfStates || !getAlphabet().contains(read) && read != null || !getAlphabet().contains(write) && write != null)
                    throw new IllegalArgumentException();

                Transaction transaction= new Transaction(fromState,read,toState, write,move);

                multibandTransactionList.add(transaction);


            }

            @Override
            public int getNumberOfStates() {
                return numberOfStates;
            }

            @Override
            public int getNumberOfTapes() {
                return numberOfTapes;
            }

            @Override
            public void setNumberOfStates(int numStates) throws IllegalArgumentException {
                if (numStates > 0)
                    numberOfStates = numStates;
                else
                    throw new IllegalArgumentException();
            }

            @Override
            public void setNumberOfTapes(int numTapes) throws IllegalArgumentException {
                if (numTapes > 0)
                    numberOfTapes = numTapes;
                else
                    throw new IllegalArgumentException();
            }

            @Override
            public void setHaltingState(int haltingState) throws IllegalArgumentException {
                //TODO: * Setzt die Nummer des (akzeptierenden) Haltezustandes
                //     * @param state Nummer des Haltezustandes
                //     * @throws IllegalArgumentException falls state nicht möglich ist
                if (haltingState < 0 || haltingState == numberOfStates)
                    throw new IllegalArgumentException();
                else
                    haltState = haltingState;
            }

            @Override
            public void setInitialState(int initialState) throws IllegalArgumentException {
                //TODO:* Setzt den initialen Zustand der Maschine.
                //     *
                //     * @param state Startzustand
                //     * @throws IllegalArgumentException falls state nicht möglich ist
                if (initialState == haltState)
                    throw new IllegalArgumentException();
                else
                    initState = initialState;
            }

            @Override
            public void setInput(String content) {
                //TODO:* Setzt den initialen Inhalt des Input-Bandes und setzt den
                //     * Schreib-/Lesekopf auf das erste Zeichen des Inhaltes. "abc" liefert
                //     * somit den Inhalt "...abc..." wobei der Schreib-/Lesekopf über dem 'a'
                //     * steht. Rechts und links von "abc" befinden sich eine unendliche, aber
                //     * natürlich nicht explizit abgespeicherte, Reihe an Leerzeichen.
                //     *
                //     * @param content Der Bandinhalt des Input-Bandes als String


                if (content == null) {
                    tapeContents = new ArrayList<>();
                    Character[] rightOfHeadChars = new Character[0];
                    Character[] leftOfHeadChars = new Character[0];
                    Character belowHead = null;
                    tapeContent = new TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);
                    currentState = initState;
                } else {
                    // might be changes with next line
                    tapeContents = new ArrayList<>();
                    Character[] rightOfHeadChars;
                    if (content.length() > 1)
                        rightOfHeadChars = new Character[content.length() - 1];
                    else
                        rightOfHeadChars = new Character[0];
                    Character[] leftOfHeadChars = new Character[0];

                    for (int i = 1; i < content.length(); i++) {
                        rightOfHeadChars[i - 1] = content.charAt(i);
                    }


                    Character belowHead;
                    if (content.equals(""))
                        belowHead = null;
                    else
                        belowHead = content.charAt(0);
                    tapeContent = new TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);

                    currentState = initState;

                    tapeContents.add(tapeContent);
                }
            }


            @Override
            public void doNextStep() throws IllegalStateException {
                if (isInErrorState()) {
                    throw new IllegalStateException();
                } else {
                    //TODO: ** Führt einen Ableitungsschritt der Turingmaschine aus. Ist kein
                    //     * passender Übergang in der Übergangsfunktion vorhanden, so befindet sich
                    //     * die Maschine nach dem Schritt im Fehlerzustand (= nichtakzeptierender
                    //     * Haltezustand, der aber nicht als tatsächlicher Zustand in der Maschine
                    //     * vorkommt)

                    boolean found = false;
                    for (Transaction transaction : transactionList) {
                        if (transaction.getFromState() == currentState && transaction.getRead() == tapeContent.getBelowHead()) {
                            currentStep = transactionList.indexOf(transaction);
                            found = true;
                            break;
                        }
                    }

                    if (transactionList.get(currentStep).getToState() == 0) {
                        isInHaltingState = true;
                    }

                    if (!found) {
                        isInErrorState = true;
                    }

                    switch (transactionList.get(currentStep).getMove()) {
                        case Left -> {
                            if (tapeContent.getLeftOfHead().length == 0) {
                                tapeContents.remove(tapeContent);
                                Character[] rightOfHeadChars;
                                Character[] leftOfHeadChars = new Character[0];

                                if (transactionList.get(currentStep).getWrite() != null) {
                                    rightOfHeadChars = new Character[tapeContent.getRightOfHead().length + 1];
                                    if (rightOfHeadChars.length == 1)
                                        rightOfHeadChars[0] = transactionList.get(currentStep).getWrite();
                                    else {
                                        rightOfHeadChars[0] = transactionList.get(currentStep).getWrite();
                                        for (int i = 0; i < tapeContent.getRightOfHead().length; i++) {
                                            rightOfHeadChars[i + 1] = tapeContent.getRightOfHead()[i];
                                        }
                                    }
                                } else{
                                    rightOfHeadChars = tapeContent.getRightOfHead();}

                                Character belowHead = null;

                                tapeContent = new TuringMachine.TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);
                                tapeContents.add(tapeContent);
                            } else {
                                tapeContents.remove(tapeContent);
                                Character[] rightOfHeadChars;
                                Character[] leftOfHeadChars = new Character[tapeContent.getLeftOfHead().length - 1];

                                if (transactionList.get(currentStep).getWrite() != null) {
                                    rightOfHeadChars = new Character[tapeContent.getRightOfHead().length + 1];
                                    if (rightOfHeadChars.length == 1)
                                        rightOfHeadChars[0] = transactionList.get(currentStep).getWrite();
                                    else {
                                        rightOfHeadChars[0] = transactionList.get(currentStep).getWrite();
                                        for (int i = 0; i < tapeContent.getRightOfHead().length; i++) {
                                            rightOfHeadChars[i + 1] = tapeContent.getRightOfHead()[i];
                                        }
                                    }
                                } else{
                                    rightOfHeadChars = tapeContent.getRightOfHead();}


                                for (int i = 0; i < tapeContent.getLeftOfHead().length - 1; i++) {
                                    leftOfHeadChars[i] = tapeContent.getLeftOfHead()[i];
                                }
                                Character belowHead = tapeContent.getLeftOfHead()[tapeContent.getLeftOfHead().length - 1];

                                tapeContent = new TuringMachine.TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);
                                tapeContents.add(tapeContent);
                            }
                            break;
                        }
                        case Stay -> {
                            tapeContents.remove(tapeContent);
                            Character[] rightOfHeadChars = new Character[tapeContent.getRightOfHead().length];
                            Character[] leftOfHeadChars = new Character[tapeContent.getLeftOfHead().length];

                            if (rightOfHeadChars.length > 0) {
                                for (int i = 0; i < tapeContent.getRightOfHead().length; i++) {
                                    rightOfHeadChars[i] = tapeContent.getRightOfHead()[i];
                                }
                            }
                            if (leftOfHeadChars.length > 0) {
                                for (int i = 0; i < tapeContent.getLeftOfHead().length; i++) {
                                    leftOfHeadChars[i] = tapeContent.getLeftOfHead()[i];
                                }
                            }

                            Character belowHead = transactionList.get(currentStep).getWrite();

                            tapeContent = new TuringMachine.TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);
                            tapeContents.add(tapeContent);
                            break;
                        }
                        case Right -> {
                            if (tapeContent.getRightOfHead().length == 0) {
                                tapeContents.remove(tapeContent);
                                Character[] rightOfHeadChars = new Character[0];
                                Character[] leftOfHeadChars;

                                if (transactionList.get(currentStep).getWrite() != null) {
                                    leftOfHeadChars = new Character[tapeContent.getLeftOfHead().length + 1];
                                    if (leftOfHeadChars.length == 1)
                                        leftOfHeadChars[0] = transactionList.get(currentStep).getWrite();

                                    else {
                                        leftOfHeadChars[leftOfHeadChars.length - 1] = transactionList.get(currentStep).getWrite();
                                        for (int i = 0; i < tapeContent.getLeftOfHead().length; i++) {
                                            leftOfHeadChars[i] = tapeContent.getLeftOfHead()[i];
                                        }
                                    }
                                } else {
                                    leftOfHeadChars = tapeContent.getLeftOfHead();
                                }

                                Character belowHead = null;

                                tapeContent = new TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);
                                tapeContents.add(tapeContent);
                            } else {
                                tapeContents.remove(tapeContent);
                                Character[] rightOfHeadChars = new Character[tapeContent.getRightOfHead().length - 1];
                                Character[] leftOfHeadChars;

                                if (transactionList.get(currentStep).getWrite() != null) {
                                    leftOfHeadChars = new Character[tapeContent.getLeftOfHead().length + 1];
                                    if (leftOfHeadChars.length == 1)
                                        leftOfHeadChars[0] = transactionList.get(currentStep).getWrite();

                                    else {
                                        leftOfHeadChars[leftOfHeadChars.length - 1] = transactionList.get(currentStep).getWrite();
                                        for (int i = 0; i < tapeContent.getLeftOfHead().length; i++) {
                                            leftOfHeadChars[i] = tapeContent.getLeftOfHead()[i];
                                        }
                                    }
                                } else
                                    leftOfHeadChars = tapeContent.getLeftOfHead();


                                if (rightOfHeadChars.length == 1) {
                                    rightOfHeadChars[0] = tapeContent.getRightOfHead()[1];
                                } else {
                                    for (int i = 1; i < tapeContent.getRightOfHead().length; i++) {
                                        rightOfHeadChars[i - 1] = tapeContent.getRightOfHead()[i];
                                    }
                                }

                                Character belowHead = tapeContent.getRightOfHead()[0];

                                tapeContent = new TapeContent(leftOfHeadChars, belowHead, rightOfHeadChars);
                                tapeContents.add(tapeContent);
                            }
                            break;
                        }
                    }
                    found = false;
                    currentState = transactionList.get(currentStep).getToState();
                }
            }

            @Override
            public boolean isInHaltingState() {
                if (isInHaltingState)
                    return true;
                else
                    return false;
            }

            @Override
            public boolean isInErrorState() {
                if (isInErrorState)
                    return true;
                else
                    return false;
            }

            @Override
            public List<TapeContent> getTapeContents() {
                if (isInErrorState()) {
                    return null;
                } else {
                    return tapeContents;
                }
            }

            @Override
            public TapeContent getTapeContent(int tape) {
                if (isInErrorState())
                    return null;
                else
                    return tapeContents.get(tape);
            }
        };
        return turingMachine;
    }


    class Transaction {
        int fromState;
        Character read;
        int toState;
        Character write;
        TuringMachine.Movement move;
        Character [] moreBandRead;
        Character[] moreBandWrite;
        TuringMachine.Movement [] moreBandMove;


        public Transaction(int fromState, Character read, int toState, Character write, TuringMachine.Movement move) {
            this.fromState = fromState;
            this.read = read;
            this.toState = toState;
            this.write = write;
            this.move = move;
        }

        public Transaction(int fromState, Character [] moreBandRead, int toState,Character [] moreBandWrite, TuringMachine.Movement [] moreBandMove){
            this.fromState=fromState;
            this.toState=toState;
            this.moreBandRead=moreBandRead;
            this.moreBandWrite=moreBandWrite;
            this.moreBandMove=moreBandMove;
        }

        public int getFromState() {
            return fromState;
        }

        public Character getRead() {
            return read;
        }

        public int getToState() {
            return toState;
        }

        public Character getWrite() {
            return write;
        }

        public TuringMachine.Movement getMove() {
            return move;
        }

        public Character[] getMoreBandRead() {
            return moreBandRead;
        }

        public Character[] getMoreBandWrite() {
            return moreBandWrite;
        }

        public TuringMachine.Movement[] getMoreBandMove() {
            return moreBandMove;
        }
    }
}
