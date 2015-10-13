public class Main {

    public static void main(String[] args) {
        if(args.length == 0) return;

        if(args[0].equalsIgnoreCase("-s")){
            //TODO: Start server
        }else if(args[0].equalsIgnoreCase("-c")){
            //TODO: Start client
        }else{
            System.out.println("Command not recognized - Stopping application");
        }

/*        //Testing marshalling
        Request r = new Request();
        r.s = "Testing";
        byte[] arr;
        try {
            arr = SerializationHelper.deserialize(r);
            Request re = (Request) SerializationHelper.deserialize(arr);
            if(re.s.equalsIgnoreCase(r.s)) System.out.println("Marshalling Ok");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/


    }
}
