package me.pietelite.mantle.common;

import org.antlr.v4.runtime.tree.ParseTreeListener;

public abstract class MantleListener implements ParseTreeListener {

  private final Proxy proxy;

  public MantleListener(Proxy proxy) {
    this.proxy = proxy;
  }

  final public Proxy proxy() {
    return this.proxy;
  }

}
