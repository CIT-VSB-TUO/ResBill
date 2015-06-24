/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.server;

import java.io.Serializable;

import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * DTO nesouci informace potrebne pro hlavicku stranek Spravy serveru
 * 
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class ServerHeaderDTO extends ServerDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public ServerHeaderDTO() {
    super();
  }

  /**
   * 
   * @param server
   */
  public ServerHeaderDTO(Server server) {
    super(server);
  }

  /**
   * 
   */
  @Override
  public void fill(Server server) {
    super.fill(server);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("toString()", super.toString());
    return builder.toString();
  }

}
