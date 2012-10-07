using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public enum EToolsListMessage { Add, Remove, Clear };

    public interface IToolsList
    {
        IEnumerable<DustData> getItems();
        int getCount();
        int indexOf(DustData ob);
        DustData get(int idx);
    }

    public interface MIToolsListBase
    {
        IEnumerable<DustData> getItems();
        DustData getItem();
    }
}
