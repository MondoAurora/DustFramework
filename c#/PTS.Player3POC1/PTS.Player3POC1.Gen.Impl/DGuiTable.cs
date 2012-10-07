using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;
using Dust.Tools;

namespace Dust.Gui
{
    public class DGuiTable : DGuiComponent, IGuiTable
    {
        public IGuiTableLayout layout;

        public IToolsList rows;
        public IToolsList cols;
        public IToolsList cells;

        public IGuiTableLayout getLayoutData()
        {
            return layout;
        }

        public IToolsList getCols()
        {
            return cols;
        }

        public IToolsList getRows()
        {
            return rows;
        }

        public IToolsList getCells()
        {
            return cells;
        }
    }

    public class DGuiTableRow : DustData, IGuiTableRow
    {
        public RowStyle style;

        public RowStyle getStyle()
        {
            return style;
        }
    }

    public class DGuiTableCol : DustData, IGuiTableCol
    {
        public ColumnStyle style;

        public ColumnStyle getStyle()
        {
            return style;
        }
    }

    public class DGuiTableLayout : DustData, IGuiTableLayout
    {
        public TableLayoutPanelCellBorderStyle borderStyle;

        public TableLayoutPanelCellBorderStyle getBorderStyle()
        {
            return borderStyle;
        }
    }

    public class DGuiTableCell : DustData, IGuiTableCell
    {
        public int row;
        public int rowSpan = 1;
        public int col;
        public int colSpan = 1;
        public DockStyle dockStyle = DockStyle.Fill;
        public IGuiComponent comp;

        public int getRow()
        {
            return row;
        }

        public int getRowSpan()
        {
            return rowSpan;
        }

        public int getCol()
        {
            return col;
        }

        public int getColSpan()
        {
            return colSpan;
        }

        public DockStyle getDockStyle()
        {
            return dockStyle;
        }

        public IGuiComponent getComp()
        {
            return comp;
        }
    }
}
