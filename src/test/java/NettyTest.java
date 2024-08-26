import com.lidachui.simpleRpc.client.NettyRpcClient;
import com.lidachui.simpleRpc.core.RpcClientProxy;
import com.lidachui.simpleRpc.service.User;
import com.lidachui.simpleRpc.service.UserService;

/**
 * NettyTest
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:25
 * @version: 1.0
 */
public class NettyTest {

  public static void main(String[] args){
    NettyRpcClient nettyRPCClient = new NettyRpcClient("127.0.0.1", 8899);
    RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRPCClient);
    UserService userService = rpcClientProxy.getProxy(UserService.class);
    User userByUserId = userService.getUserByUserId(10);
    System.out.println("从服务端得到的user为：" + userByUserId);
  }
}
