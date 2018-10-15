package io.left.rightmesh.libdtn.core.agents.http;

import io.left.rightmesh.libdtn.DTNConfiguration;
import io.left.rightmesh.libdtn.core.Component;
import io.left.rightmesh.libdtn.utils.nettyrouter.Router;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import static io.left.rightmesh.libdtn.DTNConfiguration.Entry.COMPONENT_ENABLE_DAEMON_HTTP_API;
import static io.left.rightmesh.libdtn.utils.nettyrouter.Dispatch.using;

/**
 * @author Lucien Loiseau on 13/10/18.
 */
public class APIDaemonHTTPAgent extends Component {


    private static final String TAG = "APIDaemonHTTPAgent";

    // ---- SINGLETON ----
    private static APIDaemonHTTPAgent instance;
    public static APIDaemonHTTPAgent getInstance() { return instance; }

    static {
        instance = new APIDaemonHTTPAgent();
        getInstance().initComponent(COMPONENT_ENABLE_DAEMON_HTTP_API);
    }

    HttpServer<ByteBuf, ByteBuf> server;

    @Override
    public String getComponentName() {
        return TAG;
    }

    @Override
    protected void componentUp() {
        super.componentUp();
        int serverPort = (Integer) DTNConfiguration.get(DTNConfiguration.Entry.API_DAEMON_HTTP_API_PORT).value();
        server = HttpServer.newServer(serverPort)
                .start(using(new Router<ByteBuf, ByteBuf>()
                        .GET("/", rootAction)
                        .GET("/help", rootAction)
                        .GET("/conf/", ConfigurationAPI.confAction)
                        .GET("/conf/:*", ConfigurationAPI.confAction)
                        .GET("/register", RegistrationAPI.registerAction)
                        .GET("/register/:*", RegistrationAPI.registerAction)
                        .GET("/unregister/", RegistrationAPI.unregisterAction)
                        .GET("/unregister/:*", RegistrationAPI.unregisterAction)
                        .GET("/cache/", StorageAPI.cacheAction)
                        .GET("/cache/:*", StorageAPI.cacheAction)
                        .GET("/get/", ApplicationAgentAPI.getAction)
                        .GET("/get/:*", ApplicationAgentAPI.getAction)
                        .GET("/fetch/", ApplicationAgentAPI.fetchAction)
                        .GET("/fetch/:*", ApplicationAgentAPI.fetchAction)
                        .POST("/send/", ApplicationAgentAPI.sendAction)
                        .notFound(handler404)));
    }

    @Override
    protected void componentDown() {
        super.componentDown();
        if (server != null) {
            server.shutdown();
        }
    }

    private Action rootAction = (params, req, res) -> {
        String[] header = {
                "         *                                                   .             \n",
                "      +. | .+         .                  .              .         .        \n",
                "  .*.... O   .. *        .                     .       .             .     \n",
                "   ...+.. `'O ..                                    .           |          \n",
                "   +.....+  | ..+                    .                  .      -*-         \n",
                "   ...+...  O ..      ---========================---       .    |          \n",
                "   *...  O'` ...*             .          .                   .             \n",
                " .    O'`  .+.    _________ ____   _____    _____ .  ___                   \n",
                "       `'O       /___  ___// __/  / __  |  / __  |  /   |                  \n",
                "   .                / /.  / /_   / /_/ /  / /_/ /  / /| | .                \n",
                "     .             / /   / __/  / _  |   / _  |   / __  |          .       \n",
                "              .   /./   / /__  / / | |  / / | |  / /  | |                  \n",
                "   |             /_/   /____/ /_/  |_| /_/  |_| /_/   |_|      .           \n",
                "  -*-                                                                *     \n",
                "   |     .           ---========================---             .          \n",
                "      .                 Terrestrial DTN - v1.0     .                    .  \n",
                "          .    .             *                    .             .          \n",
                "                                 .                         .               \n",
                "____ /\\__________/\\____ ______________/\\/\\___/\\__________________________|@\n",
                "                __                                               ---       \n",
                "         --           -            --  -      -         ---  __            \n",
                "   --  __                      ___--     RightMesh (c) 2018        --  __  \n\n",
                "REST API Available: \n",
                "------------------- \n\n",
                "/register/{sink}\n",
                "/unregister/{sink}\n",
                "/fetch/{nb}/{sink}\n",
                "/table/\n",
                "/table/registration/\n",
                "/table/registration/add/{sink}\n",
                "/table/registration/del/{sink}\n",
                "/table/linklocal/\n",
                "/table/static/\n",
                "/table/static/add/{eid-to}/{eidcla-nexthop}/\n",
                "/table/static/del/{eid-to}/{eidcla-nexthop}/\n",
                "/cache/\n",
                "/cache/size/\n",
                "/cache/destination/{eid}\n",
                "/cache/source/{eid}\n",
                "/cache/registration/{eid}\n",
                "/conf/\n",
                "/conf/localeid/\n",
                "/conf/localeid/{eid}\n",
                "/conf/aliases/\n",
                "/conf/aliases/add/{eid}\n",
                "/conf/aliases/del/{eid}\n",
                "/conf/maxlifetime/{max}\n",
                "/conf/maxtimestampfuture/{max}\n",
                "/conf/recvanonymous/{enable|disable}\n",
                "/conf/statusreporting/{enable|disable}\n",
                "/conf/forwarding/{enable|disable}\n",
                "/conf/blocksize/{max}\n",
                ""};
        return res.setStatus(HttpResponseStatus.OK)
                .writeString(Observable.from(header));
    };

    private Action handler404 = (params, req, res) ->
            res.setStatus(HttpResponseStatus.NOT_FOUND);
}