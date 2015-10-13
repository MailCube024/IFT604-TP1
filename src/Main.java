public class Main {

    public static void main(String[] args) {
        if(args.length == 0) return;

        if(args[0] == "-s"){
            //TODO: Start server
        }else if(args[0] == "-c"){
            //TODO: Start client
        }else{
            System.out.println("Command not recognized - Stopping application");
        }

    }
}
