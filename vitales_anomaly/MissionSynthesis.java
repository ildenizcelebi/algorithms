import java.util.*;
import java.util.stream.Collectors;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();
        ArrayList<MolecularStructure> allStructures = new ArrayList<>();
        ArrayList<Molecule> weakestMolecules = new ArrayList<>();
        allStructures.addAll(humanStructures);
        allStructures.addAll(diffStructures);

        Molecule minStrengthMolecule = null;
        double minStrength = Double.MAX_VALUE;

        for (MolecularStructure structure : allStructures) {
            Molecule weakest = structure.getMoleculeWithWeakestBondStrength();
            if (weakest != null) {
                weakestMolecules.add(weakest);
                double bondStrength = weakest.getBondStrength();
                if (bondStrength < minStrength) {
                    minStrength = bondStrength;
                    minStrengthMolecule = weakest;
                }
            }
        }

        for (Molecule molecule : weakestMolecules) {
            if (!molecule.equals(minStrengthMolecule)) {
                double bondStrength = (minStrengthMolecule.getBondStrength() + molecule.getBondStrength()) / 2.0;
                serum.add(new Bond(minStrengthMolecule, molecule, bondStrength));
            }
        }
        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        /* YOUR CODE HERE */
        System.out.println("Typical human molecules selected for synthesis: " + getMoleculeIds(humanStructures));
        System.out.println("Vitales molecules selected for synthesis: " + getMoleculeIds(diffStructures));
        System.out.println("Synthesizing the serum...");

        double totalBondStrength = 0;
        for (Bond bond : serum) {
            System.out.printf("Forming a bond between %s - %s with strength %.2f%n",
                    bond.getTo().compareTo(bond.getFrom())<0 ? bond.getTo().getId() : bond.getFrom().getId(),
                    bond.getTo().compareTo(bond.getFrom())>0 ? bond.getTo().getId() : bond.getFrom().getId(),
                    bond.getWeight());
            totalBondStrength += bond.getWeight();
        }

        System.out.printf("The total serum bond strength is %.2f%n", totalBondStrength);


    }
    private List<String> getMoleculeIds(List<MolecularStructure> structures) {
        List<String> ids = new ArrayList<>();
        for (MolecularStructure structure : structures) {
            Molecule selected = structure.getMoleculeWithWeakestBondStrength();
            ids.add(selected.getId());
        }
        return ids;
    }
}
