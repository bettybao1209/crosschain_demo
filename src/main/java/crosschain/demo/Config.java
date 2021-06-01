package crosschain.demo;

import io.neow3j.contract.ScriptHash;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.wallet.Account;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties
@Configuration
@Data
public class Config {

    private String rpcUrl;
    private String wif;
    private String proxyHashStr;
    private String nNeoHashStr;
    private String N3ReceiveAddressStr;
    private String N3Id;

    @Bean
    public Neow3j neow3j(){
        return Neow3j.build(new HttpService(rpcUrl));
    }

    @Bean
    public Account account(){
        return Account.fromWIF(wif).build();
    }

    @Bean
    public ScriptHash proxyHash() {
        return new ScriptHash(proxyHashStr);
    }

    @Bean
    public ScriptHash nNeoHash(){
        return new ScriptHash(nNeoHashStr);
    }

    @Bean
    public byte[] N3ReceiveAddress(){
        return new ScriptHash(N3ReceiveAddressStr).toArray();
    }
}
