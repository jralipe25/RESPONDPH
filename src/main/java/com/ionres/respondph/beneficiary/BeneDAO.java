package com.ionres.respondph.beneficiary;

import java.util.List;

public interface BeneDAO {

    List<BeneModel> getAll();

    boolean save(BeneModel admin);

    boolean update(BeneModel admin);

    boolean delete(int admin_id);

    boolean existsByUsername(String username);
}
