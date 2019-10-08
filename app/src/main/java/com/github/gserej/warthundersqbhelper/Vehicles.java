package com.github.gserej.warthundersqbhelper;

class Vehicles {

    private static int friendlyTanks;
    private static int friendlyPlanes;
    private static int hostileTanks;
    private static int hostilePlanes;

    Vehicles() {
        resetVehicles();
    }

    void resetVehicles() {
        friendlyTanks = 24;
        friendlyPlanes = 16;
        hostileTanks = 24;
        hostilePlanes = 16;
    }

    int getFriendlyTanks() {
        return friendlyTanks;
    }

    int getFriendlyPlanes() {
        return friendlyPlanes;
    }

    int getHostileTanks() {
        return hostileTanks;
    }

    int getHostilePlanes() {
        return hostilePlanes;
    }

    void friendlyTankDown() {
        friendlyTanks--;
    }

    void friendlyPlaneDown() {
        friendlyPlanes--;
    }

    void hostileTankDown() {
        hostileTanks--;
    }

    void hostilePlaneDown() {
        hostilePlanes--;
    }

}
