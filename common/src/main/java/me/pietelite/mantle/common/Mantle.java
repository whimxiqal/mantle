package me.pietelite.mantle.common;

public class Mantle {

  private static Proxy proxy;
  private static CommandSession currentSession;

  public static Proxy getProxy() {
    return proxy;
  }

  static void setProxy(Proxy proxy) {
    Mantle.proxy = proxy;
  }

  public static CommandSession session() {
    return currentSession;
  }

  static void setSession(CommandSession currentSession) {
    Mantle.currentSession = currentSession;
  }

  /**
   * Helper method to determine whether the source in {@link #session()} has permission for the
   * given permission string, according to the {@link Proxy} at {@link #getProxy()}.
   *
   * @param permission the permission
   * @return true if the session's source has permission
   */
  static boolean sourceHasPermission(String permission) {
    CommandSource source = session().getSource();
    switch (source.getType()) {
      case CONSOLE:
      case UNKNOWN:
        return true;
      case PLAYER:
        return getProxy().hasPermission(source.getUuid(), permission);
      default:
        throw new RuntimeException();
    }
  }

}
