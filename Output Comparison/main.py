import difflib


def compare_files(file1_path, file2_path):
    with open(file1_path, 'r') as file1, open(file2_path, 'r') as file2:
        lines1 = file1.readlines()
        lines2 = file2.readlines()

    differ = difflib.HtmlDiff()  # 创建一个HtmlDiff对象，用于生成HTML格式的差异报告
    diff = differ.make_file(lines1, lines2)  # 生成差异报告

    # 将差异报告写入HTML文件
    with open('diff_report.html', 'w') as report:
        report.write(diff)


# 比较文件差异
file1_path = 'out.txt'
file2_path = 'output.txt'
compare_files(file1_path, file2_path)
