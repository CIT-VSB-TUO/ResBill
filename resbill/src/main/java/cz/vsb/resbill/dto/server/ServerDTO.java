/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.server;

import java.io.Serializable;

import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ServerDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Integer           id               = null;

  private String            serverId         = null;

  private String            name             = null;

  /**
   * 
   */
  public ServerDTO() {
    super();
  }

  /**
   * 
   * @param server
   */
  public ServerDTO(Server server) {
    this();

    fill(server);
  }

  /**
   * 
   * @param server
   */
  public void fill(Server server) {
    id = server.getId();
    serverId = server.getServerId();
    name = server.getName();
  }

  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return the serverId
   */
  public String getServerId() {
    return serverId;
  }

  /**
   * @param serverId
   *          the serverId to set
   */
  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("id", id);
    builder.append("serverId", serverId);
    builder.append("name", name);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
