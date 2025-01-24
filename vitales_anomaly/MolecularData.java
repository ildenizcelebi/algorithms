import java.util.*;
import java.util.stream.Collectors;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        DisjointSet<Molecule> disjointSet = new DisjointSet<>(molecules);

        for (Molecule molecule : molecules) {
            for (String bondId : molecule.getBonds()) {
                Molecule bondedMolecule = findMoleculeById(bondId);
                if (bondedMolecule != null) {
                    disjointSet.union(molecule, bondedMolecule);
                }
            }
        }

        Map<Molecule, MolecularStructure> structureMap = new HashMap<>();
        for (Molecule molecule : molecules) {
            Molecule representative = disjointSet.find(molecule);
            if (!structureMap.containsKey(representative)) {
                structureMap.put(representative, new MolecularStructure());
            }
            structureMap.get(representative).addMolecule(molecule);
        }

        return new ArrayList<>(structureMap.values());
    }
    private Molecule findMoleculeById(String id) {
        for (Molecule molecule : molecules) {
            if (molecule.getId().equals(id)) {
                return molecule;
            }
        }
        return null;
    }


    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {

        /* YOUR CODE HERE */
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (int i = 0; i < molecularStructures.size(); i++) {
            MolecularStructure structure = molecularStructures.get(i);
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + structure); // Call toString() explicitly
        }


    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();

        for (MolecularStructure targetStructure : targetStructures) {
            if (!sourceStructures.contains(targetStructure)) {
                anomalyList.add(targetStructure);
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */
        Collections.sort(molecularStructures, Comparator.comparingInt(structure -> structure.getMolecules().size()));
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) {
            System.out.println(structure);
        }
    }

    @Override
    public String toString() {
        List<Molecule> sortedMolecules = new ArrayList<>(molecules);
        sortedMolecules.sort(Comparator.comparing(Molecule::getId)); // Sort molecules based on ID
        return sortedMolecules.toString();
    }

    static class DisjointSet<T> {
        private final Map<T, T> parent;

        public DisjointSet(List<T> elements) {
            parent = new HashMap<>();
            for (T element : elements) {
                parent.put(element, element);
            }
        }

        public T find(T element) {
            if (parent.get(element) == element) {
                return element;
            }
            parent.put(element, find(parent.get(element)));
            return parent.get(element);
        }

        public void union(T x, T y) {
            T xParent = find(x);
            T yParent = find(y);
            if (!xParent.equals(yParent)) {
                parent.put(xParent, yParent);
            }
        }
    }
}
