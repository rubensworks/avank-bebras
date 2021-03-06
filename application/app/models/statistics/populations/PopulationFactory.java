package models.statistics.populations;




/**
 * Factory for general populations.
 * @author Felix Van der Jeugt
 */
public class PopulationFactory {

    private static PopulationFactory instance = null;

    /**
     * Returns the singleton instance of this factory.
     */
    public static PopulationFactory instance() {
        if(instance == null) instance = new PopulationFactory();
        return instance;
    }

    /**
     * Creates a new Population with the identifier unique for that type of
     * population.
     * @param type The type of population.
     * @param identifier The type-unique identifier.
     * @param colour The html colour literal for the population.
     * @return A new population.
     */
    public Population create(PopulationType type, String identifier,
            String colour) throws PopulationFactoryException {
        if(type == null) throw new PopulationFactoryException("Type is null");
        Population p = type.getFactory().create(identifier);
        p.setColour(colour);
        return p;
    }

    /**
     * Creates a new Population with the identifier unique for that type of
     * population.
     * @param type The type of population.
     * @param identifier The type-unique identifier.
     * @param colour The html colour literal for the population.
     * @return A new population.
     */
    public Population create(String type, String identifier, String colour)
            throws PopulationFactoryException {
        return create(PopulationType.getType(type), identifier, colour);
    }

}
