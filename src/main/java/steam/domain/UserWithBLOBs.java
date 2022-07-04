package steam.domain;

import java.io.Serializable;

public class UserWithBLOBs extends User implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gamedata
     *
     * @mbggenerated
     */
    private String gamedata;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.depot
     *
     * @mbggenerated
     */
    private String depot;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.shop_data
     *
     * @mbggenerated
     */
    private String shopData;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.equip
     *
     * @mbggenerated
     */
    private String equip;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.archive
     *
     * @mbggenerated
     */
    private String archive;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gamedata
     *
     * @return the value of user.gamedata
     *
     * @mbggenerated
     */
    public String getGamedata() {
        return gamedata;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gamedata
     *
     * @param gamedata the value for user.gamedata
     *
     * @mbggenerated
     */
    public void setGamedata(String gamedata) {
        this.gamedata = gamedata == null ? null : gamedata.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.depot
     *
     * @return the value of user.depot
     *
     * @mbggenerated
     */
    public String getDepot() {
        return depot;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.depot
     *
     * @param depot the value for user.depot
     *
     * @mbggenerated
     */
    public void setDepot(String depot) {
        this.depot = depot == null ? null : depot.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.shop_data
     *
     * @return the value of user.shop_data
     *
     * @mbggenerated
     */
    public String getShopData() {
        return shopData;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.shop_data
     *
     * @param shopData the value for user.shop_data
     *
     * @mbggenerated
     */
    public void setShopData(String shopData) {
        this.shopData = shopData == null ? null : shopData.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.equip
     *
     * @return the value of user.equip
     *
     * @mbggenerated
     */
    public String getEquip() {
        return equip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.equip
     *
     * @param equip the value for user.equip
     *
     * @mbggenerated
     */
    public void setEquip(String equip) {
        this.equip = equip == null ? null : equip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.archive
     *
     * @return the value of user.archive
     *
     * @mbggenerated
     */
    public String getArchive() {
        return archive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.archive
     *
     * @param archive the value for user.archive
     *
     * @mbggenerated
     */
    public void setArchive(String archive) {
        this.archive = archive == null ? null : archive.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserWithBLOBs other = (UserWithBLOBs) that;
        return (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
            && (this.getGamedata() == null ? other.getGamedata() == null : this.getGamedata().equals(other.getGamedata()))
            && (this.getDepot() == null ? other.getDepot() == null : this.getDepot().equals(other.getDepot()))
            && (this.getShopData() == null ? other.getShopData() == null : this.getShopData().equals(other.getShopData()))
            && (this.getEquip() == null ? other.getEquip() == null : this.getEquip().equals(other.getEquip()))
            && (this.getArchive() == null ? other.getArchive() == null : this.getArchive().equals(other.getArchive()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getGamedata() == null) ? 0 : getGamedata().hashCode());
        result = prime * result + ((getDepot() == null) ? 0 : getDepot().hashCode());
        result = prime * result + ((getShopData() == null) ? 0 : getShopData().hashCode());
        result = prime * result + ((getEquip() == null) ? 0 : getEquip().hashCode());
        result = prime * result + ((getArchive() == null) ? 0 : getArchive().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", gamedata=").append(gamedata);
        sb.append(", depot=").append(depot);
        sb.append(", shopData=").append(shopData);
        sb.append(", equip=").append(equip);
        sb.append(", archive=").append(archive);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}