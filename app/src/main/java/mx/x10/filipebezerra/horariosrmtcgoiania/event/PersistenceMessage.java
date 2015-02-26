package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/25/2015
 * @since #
 */
public interface PersistenceMessage<E> {

    public enum PersistenceType {
        INSERTION, DELETION
    }

    void setPersistenceType(PersistenceType type);
    
    PersistenceType getPersistenceType();
    
    void setEntity(E entity);
    
    E getEntity();
    
}
