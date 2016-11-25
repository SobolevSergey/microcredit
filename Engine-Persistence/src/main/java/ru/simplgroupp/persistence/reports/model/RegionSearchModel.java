package ru.simplgroupp.persistence.reports.model;

/**
 *
 */
public class RegionSearchModel {
    public RegionSearchModel(String region, String regionName) {
        this.region = region;
        this.regionName = regionName;
    }

    private String region;
    private String regionName;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
