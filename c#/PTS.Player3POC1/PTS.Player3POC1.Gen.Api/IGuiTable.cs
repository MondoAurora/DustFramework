using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;
using Dust.Tools;

namespace Dust.Gui
{
    public interface IGuiTable : IGuiComponent
    {
        IGuiTableLayout getLayoutData();

        IToolsList getCols();
        IToolsList getRows();
        IToolsList getCells();
    }

    public interface IGuiTableRow
    {
        RowStyle getStyle();
    }

    public interface IGuiTableCol
    {
        ColumnStyle getStyle();
    }

    public interface IGuiTableLayout
    {
        TableLayoutPanelCellBorderStyle getBorderStyle();
    }

    public interface IGuiTableCell
    {
        int getRow();
        int getRowSpan();
        int getCol();
        int getColSpan();

        DockStyle getDockStyle();

        IGuiComponent getComp();
    }
}
