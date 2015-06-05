package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.vsb.resbill.util.ToStringBuilder;

public class ServerCriteria implements Serializable, Cloneable {

  private static final long serialVersionUID = -1967413195158720777L;

  private List<OrderBy>     orderBy          = Arrays.asList(new OrderBy[] { OrderBy.SERVER_ID_ASC });

  private String            serverId         = null;

  private Set<Integer>      serverIds        = null;

  private String            namePrefix       = null;

  private EnumSet<Feature>  features         = null;

  /**
   * 
   */
  @Override
  public ServerCriteria clone() throws CloneNotSupportedException {
    ServerCriteria clone = (ServerCriteria) super.clone();

    clone.setOrderBy(orderBy != null ? new ArrayList<OrderBy>(orderBy) : null);
    clone.setServerIds(serverIds != null ? new HashSet<Integer>(serverIds) : null);
    clone.setFeatures(features != null ? features.clone() : null);

    return clone;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  /**
   * @return the serverIds
   */
  public Set<Integer> getServerIds() {
    return serverIds;
  }

  /**
   * @param serverIds
   *          the serverIds to set
   */
  public void setServerIds(Set<Integer> serverIds) {
    this.serverIds = serverIds;
  }

  public String getNamePrefix() {
    return namePrefix;
  }

  public void setNamePrefix(String namePrefix) {
    this.namePrefix = namePrefix;
  }

  /**
   * @return the features
   */
  public EnumSet<Feature> getFeatures() {
    return features;
  }

  /**
   * @param features
   *          the features to set
   */
  public void setFeatures(EnumSet<Feature> features) {
    this.features = features;
  }

  /**
   * @return the orderBy
   */
  public List<OrderBy> getOrderBy() {
    return orderBy;
  }

  /**
   * @param orderBy
   *          the orderBy to set
   */
  public void setOrderBy(List<OrderBy> orderBy) {
    this.orderBy = orderBy;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("orderBy", orderBy);
    builder.append("serverId", serverId);
    builder.append("serverIds", serverIds);
    builder.append("namePrefix", namePrefix);
    builder.append("features", features);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

  /**
   * Pri hledani se vrati pouze ty servery, ktere splnuji pozadovanou vlastnost
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum Feature {
    /**
     * Servery odebírající zdroje mimo období prirazeni ke kontraktu
     */
    DAILY_USAGE_OUT_OF_CONTRACT,

    /**
     * Servery bez kontraktu
     */
    NO_CONTRACT,

    /**
     * Servery bez denniho vyuziti zdroju
     */
    NO_DAILY_USAGE,

  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  public static enum OrderBy {
    SERVER_ID_ASC,

    SERVER_ID_DESC,

    NAME_ASC,

    NAME_DESC,
  }

}
