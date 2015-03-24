package mx.x10.filipebezerra.horariosrmtcgoiania.model.dao;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

public class FavoriteBusStopTest extends AbstractDaoTestLongPk<FavoriteBusStopDao, FavoriteBusStop> {

    public FavoriteBusStopTest() {
        super(FavoriteBusStopDao.class);
    }

    @Override
    protected FavoriteBusStop createEntity(Long key) {
        FavoriteBusStop entity = new FavoriteBusStop();
        entity.setId(key);
        entity.setStopCode(1416);
        entity.setAddress("Praca Universitaria, Setor Leste Universitario - Goiania");
        entity.setStopReference("Faculdade De Direito Da Ufg");
        return entity;
    }

}
