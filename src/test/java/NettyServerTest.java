import com.lidachui.simpleRpc.core.RpcServer;
import com.lidachui.simpleRpc.core.ServiceProvider;
import com.lidachui.simpleRpc.server.NettyRpcServer;
import com.lidachui.simpleRpc.service.BlogService;
import com.lidachui.simpleRpc.service.BlogServiceImpl;
import com.lidachui.simpleRpc.service.UserService;
import com.lidachui.simpleRpc.service.UserServiceImpl;

/**
 * NettyServerTest
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:29
 * @version: 1.0
 */
public class NettyServerTest {
  public static void main(String[] args){
    UserService userService = new UserServiceImpl();
    BlogService blogService = new BlogServiceImpl();

//        Map<String, Object> serviceProvide = new HashMap<>();
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.UserService",userService);
//        serviceProvide.put("com.ganghuan.myRPCVersion2.service.BlogService",blogService);
    ServiceProvider serviceProvider = new ServiceProvider();
    serviceProvider.provideServiceInterface(userService);
    serviceProvider.provideServiceInterface(blogService);

    RpcServer RPCServer = new NettyRpcServer(serviceProvider);
    RPCServer.start(8899);
  }
}
