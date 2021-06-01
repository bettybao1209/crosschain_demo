package crosschain.demo;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.NEOAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@RestController
public class Demo {

	@Autowired
	Config config;

	public static void main(String[] args) {
		SpringApplication.run(Demo.class, args);
	}

	@PostMapping("/migrate")
	public void migrateToken(@RequestParam int amount){
		try {
			ContractInvocation invoc = new ContractInvocation.Builder(config.neow3j())
					.contractScriptHash(config.proxyHash())
					.function("lock")
					.parameters(Arrays.asList(
							ContractParameter.byteArray(config.nNeoHash().toArray()),// asset to be transferred
							ContractParameter.byteArrayFromAddress(config.account().getAddress()), //sender's address in little-endian
							ContractParameter.integer(Integer.valueOf(config.getN3Id())), // N3 chainId
							ContractParameter.byteArray(config.N3ReceiveAddress()), // recipient's address in little-endian
							ContractParameter.integer(amount) //asset amount to be locked
					))
					.account(config.account())
					.build()
					.sign();

				invoc.invoke();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@PostMapping("/mint")
	public void mintToken(@RequestParam double lockValue){
		try {
			config.account().updateAssetBalances(config.neow3j());
			ContractInvocation invoc = new ContractInvocation.Builder(config.neow3j())
					.contractScriptHash(config.nNeoHash())
					.function("mintTokens")
					.account(config.account())
					.output(new RawTransactionOutput(NEOAsset.HASH_ID, lockValue, config.nNeoHash().toAddress()))
					.build()
					.sign()
					.invoke();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
