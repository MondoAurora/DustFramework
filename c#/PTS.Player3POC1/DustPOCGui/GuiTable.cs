
using System.Windows.Forms;
using System;
using Dust.Core;

namespace Dust.Gui
{
    public class GuiTable : GuiComponent<DGuiTable>
    {
        protected TableLayoutPanel table = new TableLayoutPanel();

        protected override Control initI()
        {
            IGuiTable d = getData();

            table.CellBorderStyle = d.getLayoutData().getBorderStyle();

            foreach (DustData cd in d.getCols().getItems())
            {
                addCol(cd);
            }
            table.ColumnCount = table.ColumnStyles.Count;

            foreach (DustData rd in d.getRows().getItems())
            {
                addRow(rd);
            }
            table.RowCount = table.RowStyles.Count;

            foreach (DustData cd in d.getCells().getItems())
            {
                addCell(cd);
            }

            return table;
        }

        void addCol(DustData d)
        {
            IGuiTableCol cd = (IGuiTableCol) d;
            table.ColumnStyles.Add(cd.getStyle());
        }

        void addRow(DustData d)
        {
            IGuiTableRow rd = (IGuiTableRow)d;
            table.RowStyles.Add(rd.getStyle());
        }

        void addCell(DustData d)
        {
            IGuiTableCell cd = (IGuiTableCell)d;
            Control ctrl = cd.getComp().getGuiCtrl();
            ctrl.Dock = cd.getDockStyle();
            table.Controls.Add(ctrl, cd.getCol(), cd.getRow());

            int span;

            if (1 < (span = cd.getRowSpan()))
            {
                table.SetRowSpan(ctrl, span);
            }

            if (1 < (span = cd.getColSpan()))
            {
                table.SetColumnSpan(ctrl, span);
            }
        }
    }
}
