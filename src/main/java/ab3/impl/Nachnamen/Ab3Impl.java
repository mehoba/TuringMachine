package ab3.impl.Nachnamen;

import java.util.List;
import java.util.Set;

import ab3.Ab3;
import ab3.TuringMachine;

public class Ab3Impl implements Ab3 {

    private TuringMachine turingMachine;
    protected int currentState, startState, numberOfStates, numberOfTapes;
    protected List<String> tape;
    protected boolean isInHaltingState, isInErrorState;
    protected Set<Character> turingAlphabet; // hashset ?
    protected int haltState, initState;
    protected char blankSymbol ='#';
    protected TuringMachine.TapeContent tapeContent;
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
        }

        @Override
        public int getCurrentState() throws IllegalStateException {
            if(isInErrorState())
                throw new IllegalStateException();
            else
                return currentState;
        }

        @Override
        public void setAlphabet(Set<Character> alphabet) throws IllegalArgumentException {
            if(alphabet.contains(null)){
                throw new IllegalArgumentException();
            }else{
                /* Gibt das Input-Alphabet der Turingmaschine. Das Bandalphabet
                 * unterscheidet sich vom Input-Alphabet nur durch das zusätzlich
                 * vorhandene Leerzeichen (das durch "null" repräsentiert wird).*/
                turingAlphabet = alphabet;
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
            if(numStates>0)
                numberOfStates = numStates;
            else
                throw new IllegalArgumentException();
        }

        @Override
        public void setNumberOfTapes(int numTapes) throws IllegalArgumentException {
            if(numTapes>0)
                numberOfTapes = numTapes;
            else
                throw new IllegalArgumentException();
        }

        @Override
        public void setHaltingState(int haltingState) throws IllegalArgumentException {
            //TODO: * Setzt die Nummer des (akzeptierenden) Haltezustandes
            //     * @param state Nummer des Haltezustandes
            //     * @throws IllegalArgumentException falls state nicht möglich ist
            if(haltingState == initState || haltingState == 0)
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
            if(initialState == haltState)
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

            // Nisam siguran da li je ovo ovako zamisljeno, ako treba ispravi
            Character[] rightOfHeadChars = new Character[]{};
            Character[] leftOfHeadChars = new Character[]{};
            for (int i = 0; i < content.length(); i++) {
                rightOfHeadChars[i] = content.charAt(i);
            }
             tapeContent = new TapeContent(leftOfHeadChars,content.charAt(0),rightOfHeadChars);
        }

        @Override
        public void doNextStep() throws IllegalStateException {
            if(isInHaltingState()||isInErrorState()){
                throw new IllegalStateException();
            }else{
                //TODO: ** Führt einen Ableitungsschritt der Turingmaschine aus. Ist kein
                //     * passender Übergang in der Übergangsfunktion vorhanden, so befindet sich
                //     * die Maschine nach dem Schritt im Fehlerzustand (= nichtakzeptierender
                //     * Haltezustand, der aber nicht als tatsächlicher Zustand in der Maschine
                //     * vorkommt)

            }
        }

        @Override
        public boolean isInHaltingState() {
            if(isInHaltingState)
                return true;
            else
                return false;
        }

        @Override
        public boolean isInErrorState() {
            if(isInErrorState)
                return true;
            else
                return false;
        }

        @Override
        public List<TapeContent> getTapeContents() {
            if(isInErrorState()){
                return null;
            }else{
                //TODO : Führende und
                //     * nachfolgende Leerzeichen sollen entfernt (bzw. optimalerweise während
                //     * der Abarbeitung garnicht gespeichert) werden.
            }
            return null;
        }

        @Override
        public TapeContent getTapeContent(int tape) {
            return null;
        }
    };
	return turingMachine;
    }
}
