import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class SyncTransaction {
    private static long lastSyncLock = 0;

    public static void start(){
        new Thread(){
            @Override
            public void run() {
                long count = 0;
                try {
                    count = WalletJava.getBlockCount();
                    if(lastSyncLock!=count){
                        String hash = WalletJava.getBlockHash(count);
                        List<WalletJava.TransactionItem> transactionItemList  = WalletJava.listSinceBlock(hash);
                        for(WalletJava.TransactionItem item:transactionItemList){
                            System.out.println(String.format("%s:%s:%s",item.address,item.category,item.amount));
                        }
                        lastSyncLock = count;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static void main(String[] args) {
        start();
    }
}
