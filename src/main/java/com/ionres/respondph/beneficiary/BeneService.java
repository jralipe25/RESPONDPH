package com.ionres.respondph.beneficiary;

import java.util.List;

public interface BeneService {

    List<BeneModel> getAllBene();

    boolean createBene(BeneModel admin);

    void updateBene(BeneModel admin);

    void deleteBeneById(int id);
}
