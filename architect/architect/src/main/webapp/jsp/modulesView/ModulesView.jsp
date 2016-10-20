<%@ page contentType="text/html; charset=UTF-8"%>
<%-- <%@ taglib prefix="s" uri="/struts-tags"%> --%>
<div id="content">
    <div id="box">
        <h3>Escenarios</h3>
        <table width="100%" class="tableScenarios"><colgroup><col style="width: 49px;"><col style="width: 195px;"><col style="width: 188px;"><col style="width: 99px;"><col style="width: 99px;"><col style="width: 69px;"></colgroup>
            <thead>
                <tr>

                    <th width="40px" class="header"><a href="#">ID<img height="16" align="absmiddle" width="16" src="resources/images/icons/arrow_down_mini.gif"></a></th>
                    <th class="header">Estado</th>
                    <th class="header"><a href="#">Descripci&oacute;n</a></th>
                    <th width="90px" class="header"><a href="#">Tipo</a></th>
                    <th width="90px" class="header"><a href="#">Prioridad</a></th>
                    <th width="60px"></th>
                </tr>
            </thead>
            <tbody>

                <tr id="tr16" style="background: rgb(255, 255, 255) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;">
                    <td class="a-center"><input type="checkbox" class="checkScenario" id="check16" name="check16"> 16</td>
                    <td class="a-center state16"><img src="resources/images/ok.png"> Completado</td>
                    <td class="a-center description16"> Escenario (P1)</td>
                    <td class="a-center type16">Performance</td>
                    <td class="a-center priority16"><img src="resources/images/icons/Alta.gif" style="vertical-align: middle;"> Alta</td>
                    <td class="a-center">
                        <a class="scenarioDetail" rel="16" href="#"><img src="resources/images/icons/information.png" tiptitle="Ver detalle"></a>
                        <a href="#" rel="16" class="editScenario"><img src="resources/images/icons/pencil.png" tiptitle="Editar escenario"></a>
                        <a href="#" rel="16" class="deleteScenario"><img src="resources/images/icons/delete.png" tiptitle="Eliminar escenario"></a>
                    </td>
                </tr>

                <tr id="tr12" style="background: rgb(246, 246, 246) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;">
                    <td class="a-center"><input type="checkbox" class="checkScenario" id="check12" name="check12"> 12</td>
                    <td class="a-center state12"><img src="resources/images/ko.png"> Conflicto</td>
                    <td class="a-center description12"> Escenario (M1)</td>
                    <td class="a-center type12">Modificabilidad</td>
                    <td class="a-center priority12"><img src="resources/images/icons/Alta.gif" style="vertical-align: middle;"> Alta</td>
                    <td class="a-center">
                        <a class="scenarioDetail" rel="12" href="#"><img src="resources/images/icons/information.png" tiptitle="Ver detalle"></a>
                        <a href="#" rel="12" class="editScenario"><img src="resources/images/icons/pencil.png" tiptitle="Editar escenario"></a>
                        <a href="#" rel="12" class="deleteScenario"><img src="resources/images/icons/delete.png" tiptitle="Eliminar escenario"></a>
                    </td>
                </tr>

                <tr id="tr13" style="background: rgb(255, 255, 255) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;">
                    <td class="a-center"><input type="checkbox" class="checkScenario" id="check13" name="check13"> 13</td>
                    <td class="a-center state13"><img src="resources/images/okko.png"> Sin modificacion</td>
                    <td class="a-center description13"> Escenario (M2)</td>
                    <td class="a-center type13">Modificabilidad</td>
                    <td class="a-center priority13"><img src="resources/images/icons/Baja.gif" style="vertical-align: middle;"> Baja</td>
                    <td class="a-center">
                        <a class="scenarioDetail" rel="13" href="#"><img src="resources/images/icons/information.png" tiptitle="Ver detalle"></a>
                        <a href="#" rel="13" class="editScenario"><img src="resources/images/icons/pencil.png" tiptitle="Editar escenario"></a>
                        <a href="#" rel="13" class="deleteScenario"><img src="resources/images/icons/delete.png" tiptitle="Eliminar escenario"></a>
                    </td>
                </tr>

                <tr id="tr14" style="background: rgb(246, 246, 246) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;">
                    <td class="a-center"><input type="checkbox" class="checkScenario" id="check14" name="check14"> 14</td>
                    <td class="a-center state14"><img src="resources/images/none.png"> Sin procesar</td>
                    <td class="a-center description14"> Escenario (P1)</td>
                    <td class="a-center type14">Performance</td>
                    <td class="a-center priority14"><img src="resources/images/icons/Media.gif" style="vertical-align: middle;"> Media</td>
                    <td class="a-center">
                        <a class="scenarioDetail" rel="14" href="#"><img src="resources/images/icons/information.png" tiptitle="Ver detalle"></a>
                        <a href="#" rel="14" class="editScenario"><img src="resources/images/icons/pencil.png" tiptitle="Editar escenario"></a>
                        <a href="#" rel="14" class="deleteScenario"><img src="resources/images/icons/delete.png" tiptitle="Eliminar escenario"></a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <br>
    <div class="moduleResult">
        <img src="resources/images/diagrama_archivos_insumo.jpg" />
    </div>
</div>