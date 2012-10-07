
using System.Windows.Forms;
using System;
using Dust.Core;
using System.Drawing;
using System.Windows.Markup;
using System.IO;
using System.Windows.Forms.Integration;
using System.Windows;
using System.Windows.Controls;

namespace Dust.Gui
{
    public class GuiXaml : GuiComponent<DGuiXaml>
    {
        protected String xamlPath;
        ElementHost eh = new ElementHost();

        public GuiXaml() { }

        public GuiXaml(String text)
        {
            xamlPath = text;
        }

        protected override System.Windows.Forms.Control initI()
        {
            IGuiXaml xd = getData();

            xamlPath = xd.getXamlPath();

            Stream xamlIn = new FileStream(xamlPath, FileMode.Open);
            Object ob = XamlReader.Load(xamlIn);

            Frame f = new Frame();
            f.Content = (UIElement)ob;
            eh.Child = f;

            return eh;
        }
    }
}
