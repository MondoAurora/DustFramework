using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Dust.Core;
using Dust.Tools;
using Dust.Gui;

namespace PTS.Player3POC1.Demo2
{
    public class PTSTerminal : DustCompData
    {
        public IToolsEnvironment environment;

        public IToolsList ContentList;
        public IGuiComponent rootGui;
        public IToolsList eventHandlers;
    }
}
