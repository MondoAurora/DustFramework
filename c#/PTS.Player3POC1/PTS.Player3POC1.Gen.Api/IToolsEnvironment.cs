﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public interface IToolsEnvironment
    {
        IToolsScheduler getScheduler();
    }
}
