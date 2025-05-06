# LLM-Mutator-Empirical-Comparison-Artifacts

This repository contains all the raw artifacts, data processing notebooks, and result files that can be referred to replicate the study:

**"Evaluating the Effectiveness of LLM-Generated Code Mutations in Software Testing: An Empirical Comparison with PIT"**

This replication package is intended for readers and researchers who wish to understand, inspect, or reproduce the analysis and findings described in the study.

---

## Repository Structure

| Folder/File | Description                                                                                                                 |
|-------------|-----------------------------------------------------------------------------------------------------------------------------|
| `Jupyter_Notebooks/` | Contains Python Jupyter notebooks used for cleaning, processing, and analyzing mutation data.                               |
| `LLM_Mutated_Repos_Raw_Uncleaned(Refer_Only)/` | Raw LLM-mutated Java repositories (uncleaned). These are included for reference purposes only.                              |
| `Repos_Under_Test/` | The original five Java repositories used in the study for applying mutation testing.                                        |
| `Results/` | Final processed mutation scores, comparisons between PIT and LLM mutants, and any tables or figures referenced in the paper. |
| `LICENSE` | MIT License.                                                                                                                |
| `README.md` | This file.                                                                      |

---

## How to Use

### 1. Clone the Repository

```bash
git clone https://github.com/ParinayKarande/LLM-Mutator-Empirical-Comparison-Artifacts.git
cd LLM-Mutator-Empirical-Comparison-Artifacts
```

### 2. Open the Jupyter Notebooks

Navigate to the `Jupyter_Notebooks/` directory and open the notebooks to explore data processing and result generation.

You can launch the notebook environment with:

```bash
jupyter notebook
```

---

## Related Resources

-  Main LLM Code Mutator Application (Java): [https://github.com/ParinayKarande/LLM-Code-Mutator](https://github.com/ParinayKarande/LLM-Code-Mutator)

---

## Citation

If you use this package or refer to the results in your own work, please cite the original paper and this repository:

```bibtex
@misc{Parinay2025llmreplication,
  author       = {Parinay Karande},
  title        = {LLM-Mutator Empirical Comparison Artifacts},
  year         = 2025,
  howpublished = {\url{https://github.com/ParinayKarande/LLM-Mutator-Empirical-Comparison-Artifacts}},
  note         = {Includes raw data, notebooks, and result files for reproducibility}
}
```

---

## 🪪 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.