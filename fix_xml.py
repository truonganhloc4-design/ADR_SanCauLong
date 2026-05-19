import re

with open('app/src/main/res/layout/fragment_so_do.xml', 'r', encoding='utf-8') as f:
    content = f.read()

def get_buttons(i):
    return f'''                            <!-- Sân tr?ng: 2 nút -->
                            <LinearLayout
                                android:id="@+id/btnGroupSan{i}Available"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:orientation="horizontal"
                                android:weightSum="2">

                                <TextView
                                    android:id="@+id/btnCourt{i}DatLich"
                                    android:layout_width="0dp"
                                    android:layout_height="32dp"
                                    android:layout_weight="1"
                                    android:layout_marginEnd="4dp"
                                    android:background="@drawable/bg_btn_outlined"
                                    android:text="Ð?t l?ch"
                                    android:textColor="@color/primary"
                                    android:textSize="12sp"
                                    android:gravity="center"
                                    android:textStyle="bold" />

                                <TextView
                                    android:id="@+id/btnCourt{i}NhanSan"
                                    android:layout_width="0dp"
                                    android:layout_height="32dp"
                                    android:layout_weight="1"
                                    android:layout_marginStart="4dp"
                                    android:background="@drawable/bg_btn_primary"
                                    android:text="Nh?n sân"
                                    android:textColor="@color/white"
                                    android:textSize="12sp"
                                    android:gravity="center"
                                    android:textStyle="bold" />
                            </LinearLayout>

                            <!-- Sân dang choi: 1 nút -->
                            <TextView
                                android:id="@+id/btnCourt{i}ChiTiet"
                                android:layout_width="match_parent"
                                android:layout_height="32dp"
                                android:background="@drawable/bg_btn_outlined"
                                android:text="Chi ti?t"
                                android:textColor="@color/text_secondary"
                                android:textSize="12sp"
                                android:gravity="center"
                                android:textStyle="bold"
                                android:visibility="gone" />'''

# For Sân 1, 3, 4: they have btnGroupSanXAvailable but NO btnCourtXChiTiet
for i in [1, 3, 4]:
    pattern = r'<!-- Sân tr?ng: 2 nút -->\s*<LinearLayout\s+android:id="@+id/btnGroupSan' + str(i) + r'Available"[\s\S]*?</LinearLayout>'
    content = re.sub(pattern, get_buttons(i), content)

# For Sân 2: it has btnCourt2ChiTiet but NO btnGroupSan2Available
pattern2 = r'<!-- Sân dang choi: 1 nút -->\s*<TextView\s+android:id="@+id/btnCourt2ChiTiet"[\s\S]*?/>'
content = re.sub(pattern2, get_buttons(2), content)

with open('app/src/main/res/layout/fragment_so_do.xml', 'w', encoding='utf-8') as f:
    f.write(content)
