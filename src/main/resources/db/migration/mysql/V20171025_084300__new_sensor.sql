DELETE FROM m_formula_param WHERE sensor_id in (110, 111, 112, 113, 114, 115, 116);
DELETE FROM m_formula_sensor WHERE sensor_id in (110, 111, 112, 113, 114, 115, 116);
DELETE FROM m_sensorinfo WHERE sensorPhysicalid in (110, 111, 112, 113, 114, 115, 116);

INSERT INTO m_sensorinfo (sensorPhysicalid, en_name, cn_name, sensorPrecision, units, showType, signType, `maxValue`, `minValue`, rangeType)
SELECT 110, 'MC', '含水率', 1, '%', 0, 1, 100, 0, 3 FROM DUAL UNION ALL
SELECT 111, 'A-X', 'X轴加速度', 1, 'mg', 0, 1, 8000, -8000, 3 FROM DUAL UNION ALL
SELECT 112, 'A-Y', 'Y轴加速度', 1, 'mg', 0, 1, 8000, -8000, 3 FROM DUAL UNION ALL
SELECT 113, 'A-Z', 'Z轴加速度', 1, 'mg', 0, 1, 8000, -8000, 3 FROM DUAL UNION ALL
SELECT 114, 'Vibration', '设备振动', 1, '', 0, 1, 1, 0, 3 FROM DUAL UNION ALL
SELECT 115, 'IR', '红外开关', 1, '', 0, 1, 1, 0, 3 FROM DUAL UNION ALL
SELECT 116, 'SM', '微动开关', 1, '', 0, 1, 1, 0, 3 FROM DUAL;

INSERT INTO m_formula_sensor(sensor_id, formula_id, min_x, max_x, x_range_type, min_y, max_y, y_range_type, sign_type)
SELECT 110, 1, 0, 65535, 3, 0, 100, 3, 1 FROM dual UNION ALL
SELECT 111, 1, -8000, 8000, 3, -8000, 8000, 3, 1 FROM dual UNION ALL
SELECT 112, 1, -8000, 8000, 3, -8000, 8000, 3, 1 FROM dual UNION ALL
SELECT 113, 1, -8000, 8000, 3, -8000, 8000, 3, 1 FROM dual UNION ALL
SELECT 114, 1, 0, 1, 3, 0, 1, 3, 1 FROM dual UNION ALL
SELECT 115, 1, 0, 1, 3, 0, 1, 3, 1 FROM dual UNION ALL
SELECT 116, 1, 0, 1, 3, 0, 1, 3, 1 FROM dual;

INSERT INTO m_formula_param(sensor_id, name, value)
SELECT 110, 'a', 1 FROM dual UNION ALL
SELECT 110, 'b', 0 FROM dual UNION ALL
SELECT 111, 'a', 1 FROM dual UNION ALL
SELECT 111, 'b', 0 FROM dual UNION ALL
SELECT 112, 'a', 1 FROM dual UNION ALL
SELECT 112, 'b', 0 FROM dual UNION ALL
SELECT 113, 'a', 1 FROM dual UNION ALL
SELECT 113, 'b', 0 FROM dual UNION ALL
SELECT 114, 'a', 1 FROM dual UNION ALL
SELECT 114, 'b', 0 FROM dual UNION ALL
SELECT 115, 'a', 1 FROM dual UNION ALL
SELECT 115, 'b', 0 FROM dual UNION ALL
SELECT 116, 'a', 1 FROM dual UNION ALL
SELECT 116, 'b', 0 FROM dual;