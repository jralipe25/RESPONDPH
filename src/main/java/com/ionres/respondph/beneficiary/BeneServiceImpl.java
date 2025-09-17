package com.ionres.respondph.beneficiary;

import java.util.List;

public class BeneServiceImpl implements BeneService{
    BeneDAO beneDao = new BeneDAOImpl();

    @Override
    public List<BeneModel> getAllBene() {
        List<BeneModel> benes = beneDao.getAll();
        return benes;
    }

    @Override
    public boolean createBene(BeneModel bene) {
//        if (bene.getUsername() == null || bene.getUsername().isBlank()) {
//            throw new IllegalArgumentException("Username is required");
//        }
//
//        if (beneDao.existsByUsername(bene.getUsername())) {
//            throw new IllegalStateException("Username already exists");
//        }
//
//        boolean flag = beneDao.save(bene);
//        if (!flag) {
//            throw new RuntimeException("Failed to save bene");
//        }
        
        return true;
    }

    @Override
    public void updateBene(BeneModel bene) {
        
    }

    @Override
    public void deleteBeneById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
