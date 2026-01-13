package com.example.demo.handler;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 依据某个字段值（通常是ID），合并指定列的单元格
 */
public class FlexibleMergeStrategy implements CellWriteHandler {

    private List<?> dataList;        // 导出的数据集合
    private String keyFieldName;     // 依据哪个字段判断是否重复 (例如 "projectId")
    private int[] mergeColIndices;   // 哪些列需要合并 (例如 0, 1)

    public FlexibleMergeStrategy(List<?> dataList, String keyFieldName, int[] mergeColIndices) {
        this.dataList = dataList;
        this.keyFieldName = keyFieldName;
        this.mergeColIndices = mergeColIndices;
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        // 排除表头和非数据行
        if (context.getHead() || context.getRelativeRowIndex() == null) return;

        int curColIndex = context.getCell().getColumnIndex();
        int relativeRowIndex = context.getRelativeRowIndex(); // 当前是第几条数据

        // 1. 判断当前列是否需要合并
        if (!isColNeedMerge(curColIndex)) return;

        // 2. 获取当前行的 Key 值
        Object currentData = dataList.get(relativeRowIndex);
        Object currentKey = getFieldValue(currentData, keyFieldName);
        if (currentKey == null) return;

        // 3. 判断是否是该组数据的"第一行" (Group Head)
        // 如果是第一行，计算后面有多少行Key相同，然后一次性合并
        boolean isGroupHead = false;
        if (relativeRowIndex == 0) {
            isGroupHead = true;
        } else {
            Object preData = dataList.get(relativeRowIndex - 1);
            Object preKey = getFieldValue(preData, keyFieldName);
            if (!currentKey.equals(preKey)) {
                isGroupHead = true;
            }
        }

        // 4. 如果是组头，执行合并逻辑
        if (isGroupHead) {
            int count = 1;
            // 向下查找有多少条相同的数据
            for (int i = relativeRowIndex + 1; i < dataList.size(); i++) {
                Object nextData = dataList.get(i);
                Object nextKey = getFieldValue(nextData, keyFieldName);
                if (nextKey != null && nextKey.equals(currentKey)) {
                    count++;
                } else {
                    break;
                }
            }

            // 如果有多行相同，进行合并
            if (count > 1) {
                Sheet sheet = context.getWriteSheetHolder().getSheet();
                int rowIndex = context.getRowIndex(); // 实际 Excel 行号
                // 合并单元格: (起始行, 结束行, 起始列, 结束列)
                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        rowIndex, rowIndex + count - 1, curColIndex, curColIndex
                );
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }

    private boolean isColNeedMerge(int colIndex) {
        for (int i : mergeColIndices) {
            if (i == colIndex) return true;
        }
        return false;
    }

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            // 如果父类字段，可能需要 getSuperclass()，这里简化处理
            return null;
        }
    }
}
