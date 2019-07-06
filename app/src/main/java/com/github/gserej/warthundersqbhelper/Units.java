package com.github.gserej.warthundersqbhelper;

class Units {

    private int ourTanks;
    private int ourPlanes;
    private int enemyTanks;
    private int enemyPlanes;

    Units() {
        this.ourTanks = 24;
        this.ourPlanes = 16;
        this.enemyTanks = 24;
        this.enemyPlanes = 16;
    }

    void resetUnits() {
        this.ourTanks = 24;
        this.ourPlanes = 16;
        this.enemyTanks = 24;
        this.enemyPlanes = 16;
    }

    int getOurTanks() {
        return ourTanks;
    }

    int getOurPlanes() {
        return ourPlanes;
    }

    int getEnemyTanks() {
        return enemyTanks;
    }

    int getEnemyPlanes() {
        return enemyPlanes;
    }

    void ourTankDown() {
        ourTanks--;
    }

    void ourPlaneDown() {
        ourPlanes--;
    }

    void enemyTankDown() {
        enemyTanks--;
    }

    void enemyPlaneDown() {
        enemyPlanes--;
    }

}
