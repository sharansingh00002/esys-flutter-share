import Flutter
import UIKit

public class SwiftEsysFlutterSharePlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "channel:github.com/orgs/esysberlin/esys-flutter-share", binaryMessenger: registrar.messenger())
        let instance = SwiftEsysFlutterSharePlugin()
        
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if(call.method == "file"){
            self.file(arguments: call.arguments)
        }
    }
    func file(arguments:Any?) -> Void {
        // prepare method channel args
        // no use in ios
        //// let title:String = argsMap.value(forKey: "title") as! String
        let argsMap = arguments as! NSDictionary
        let name:String = argsMap.value(forKey: "name") as! String
        let text:String = argsMap.value(forKey: "text") as! String
        let packageName:String = argsMap.value(forKey: "packageName") as! String

        if((packageName.count > 0)){

            print("this works 1");
            let msg = text
            let urlWhats = "whatsapp://send?text=\(msg)"



            if let urlString = urlWhats.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed) {
                print("this works 2");
              if let whatsappURL = URL(string: urlString) {
                if UIApplication.shared.canOpenURL(whatsappURL) {
                    UIApplication.shared.openURL(whatsappURL)
                } else {
                  // Cannot open whatsapp
                    let docsPath:String = NSSearchPathForDirectoriesInDomains(.cachesDirectory,.userDomainMask , true).first!;
                                let contentUri = NSURL(fileURLWithPath: docsPath).appendingPathComponent(name)

                                // prepare sctivity items
                                var activityItems:[Any] = [contentUri!];
                                if(!text.isEmpty){
                                    // add optional text
                                    activityItems.append(text);
                                }

                                // set up activity view controller
                                let activityViewController:UIActivityViewController = UIActivityViewController(activityItems: activityItems, applicationActivities: nil)

                                // present the view controller
                                let controller = UIApplication.shared.keyWindow!.rootViewController as! FlutterViewController
                                activityViewController.popoverPresentationController?.sourceView = controller.view

                                controller.show(activityViewController, sender: self)
                }
              }
            }


        }else{
                // load the file
                let docsPath:String = NSSearchPathForDirectoriesInDomains(.cachesDirectory,.userDomainMask , true).first!;
                let contentUri = NSURL(fileURLWithPath: docsPath).appendingPathComponent(name)

                // prepare sctivity items
                var activityItems:[Any] = [contentUri!];
                if(!text.isEmpty){
                    // add optional text
                    activityItems.append(text);
                }

                // set up activity view controller
                let activityViewController:UIActivityViewController = UIActivityViewController(activityItems: activityItems, applicationActivities: nil)

                // present the view controller
                let controller = UIApplication.shared.keyWindow!.rootViewController as! FlutterViewController
                activityViewController.popoverPresentationController?.sourceView = controller.view

                controller.show(activityViewController, sender: self)
            }
        }

}
