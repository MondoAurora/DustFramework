
using Dust.Tools;
using Microsoft.Win32;
using Dust.Core;

namespace Dust.Tools
{
    public class ToolsHtmlLocal : ToolsWorker <DustCompData>
    {
        protected override void loopInit() {
            RegistryKey registry = Registry.CurrentUser.OpenSubKey("Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", true);
            registry.SetValue("ProxyEnable", 1);
            registry.SetValue("ProxyServer", "127.0.0.1:8080");
        }

        protected override void loop()
        {
            // status update, self ping, etc.
        }

        protected override void loopEnd(bool normal)
        {
            RegistryKey registry = Registry.CurrentUser.OpenSubKey("Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", true);
            registry.SetValue("ProxyEnable", 0);
        }
    }
}
