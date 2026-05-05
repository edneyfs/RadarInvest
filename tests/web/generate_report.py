import xml.etree.ElementTree as ET
import os
from datetime import datetime

def generate_summary():
    # Caminho do arquivo XML gerado pelo Robot
    xml_file = 'logs/output.xml'
    output_file = 'logs/resumo_execucao.txt'

    if not os.path.exists(xml_file):
        print(f"Arquivo {xml_file} não encontrado. Rode os testes primeiro!")
        return

    tree = ET.parse(xml_file)
    root = tree.getroot()

    timestamp = datetime.now().strftime("%d/%m/%Y %H:%M:%S")
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("==================================================\n")
        f.write(f"RESUMO DA EXECUÇÃO - {timestamp}\n")
        f.write("==================================================\n\n")

        total_tests = 0
        passed_tests = 0
        failed_tests = 0

        # Estatísticas Gerais (iterando sobre todos os testes)
        for stat in root.iter('total'):
             # O Robot joga totais no final, pegamos o stat geral
             pass 

        # Detalhes dos Falhas
        f.write("--- FALHAS ENCONTRADAS ---\n")
        has_failure = False

        for test in root.iter('test'):
            total_tests += 1
            status = test.find('status')
            if status is not None:
                if status.attrib['status'] == 'PASS':
                    passed_tests += 1
                elif status.attrib['status'] == 'FAIL':
                    failed_tests += 1
                    has_failure = True
                    test_name = test.attrib.get('name', 'Desconhecido')
                    error_msg = status.text or "Sem mensagem de erro"
                    
                    # Tenta pegar a documentação do teste
                    doc = test.find('doc')
                    doc_text = doc.text if doc is not None else "Sem documentação"

                    # Busca a keyword específica que falhou
                    # Iteramos sobre todas as kws e pegamos a última que falhou (geralmente a causa raiz)
                    failed_kw_name = "Nenhuma keyword específica identificada"
                    for kw in test.iter('kw'):
                        kw_status = kw.find('status')
                        if kw_status is not None and kw_status.attrib['status'] == 'FAIL':
                            failed_kw_name = kw.attrib.get('name', 'Keyword desconhecida')

                    f.write(f"\n[X] TESTE: {test_name}\n")
                    f.write(f"    DESCRIÇÃO: {doc_text}\n")
                    f.write(f"    FALHOU EM: {failed_kw_name}\n")
                    f.write(f"    ERRO: {error_msg.strip()}\n")
                    f.write("-" * 50 + "\n")

        if not has_failure:
            f.write("\nNENHUMA FALHA ENCONTRADA! \o/\n")

        f.write("\n==================================================\n")
        f.write(f"TOTAL: {total_tests} | PASSOU: {passed_tests} | FALHOU: {failed_tests}\n")
        f.write("==================================================\n")

    print(f"Relatório gerado em: {output_file}")
    with open(output_file, 'r', encoding='utf-8') as f:
        print(f.read())

if __name__ == "__main__":
    generate_summary()
